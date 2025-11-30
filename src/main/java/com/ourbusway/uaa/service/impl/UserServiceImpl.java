package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.mappers.UserMapper;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.TokenService;
import com.ourbusway.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDaoService userDaoService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final MailingService mailingService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<UserGetResource> register(UserRegistrationPostResource registrationRequest) {
        log.debug("Starting registration process for email: {}", registrationRequest.getEmail());

        // Business logic: Check if email already exists
        if (userDaoService.existsBy(UserSpecification.withEmail(registrationRequest.getEmail()))) {
            log.warn("Registration failed: email {} already exists", registrationRequest.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        // Map and set user properties
        UserModel user = userMapper.registrationToModel(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(RoleEnum.PASSENGER); // self-registration always creates passengers
        user.setActivated(false); // require email activation
        user.setEnabled(true); // enable by default

        user = userDaoService.save(user);
        log.info("User created successfully with email {}", user.getEmail());

        // Send activation email
        TokenModel tokenModel = tokenService.generateAccountValidationToken(user);
        mailingService.sendActivationAccountEmail(user, tokenModel);
        log.debug("Activation email sent to {}", user.getEmail());

        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    }

    @Override
    public ResponseEntity<UserGetResource> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Fetching profile for authenticated user: {}", email);

        // Get user by authenticated email
        UserModel user = userDaoService.findOneBy(UserSpecification.withEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    }

    @Override
    @Transactional
    public ResponseEntity<UserGetResource> updateProfile(UserPatchResource userPatchResource) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Updating profile for user: {}", email);

        // Get existing user and update
        UserModel existingUser = userDaoService.findOneBy(UserSpecification.withEmail(email));

        // Update user with partial data
        userMapper.patchResourceToModel(userPatchResource, existingUser);

        existingUser = userDaoService.save(existingUser);
        log.info("User updated successfully with id: {}", existingUser.getId());

        return ResponseEntity.ok(userMapper.modelToGetResource(existingUser));
    }

    @Override
    public ResponseEntity<UserGetResource> getUserById(String id){
        UserModel user = userDaoService.findOneBy(UserSpecification.withUuid(id));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    };
}