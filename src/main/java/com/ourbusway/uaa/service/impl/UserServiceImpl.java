package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.mappers.UserMapper;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.resource.user.*;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.TokenService;
import com.ourbusway.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ourbusway.uaa.enumeration.RoleEnum;

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
    public ResponseEntity<UserGetResource> register(UserRegistrationPostResource registrationRequest) {
        log.debug("Starting registration process for email: {}", registrationRequest.getEmail());

        if (userDaoService.existsBy(UserSpecification.withEmail(registrationRequest.getEmail()))) {
            log.warn("Registration failed: email {} already exists", registrationRequest.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        UserModel user = userMapper.registrationToModel(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(RoleEnum.PASSENGER);
        user.setActivated(false);
        user.setEnabled(true);

        user = userDaoService.save(user);
        log.info("User created successfully with email {}", user.getEmail());

        TokenModel tokenModel = tokenService.generateAccountValidationToken(user);
        mailingService.sendActivationAccountEmail(user, tokenModel);
        log.debug("Activation email sent to {}", user.getEmail());

        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    }

    @Override
    public ResponseEntity<UserGetResource> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Fetching profile for authenticated user: {}", email);

        UserModel user = userDaoService.findOneBy(UserSpecification.withEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    }

    @Override
    public ResponseEntity<UserGetResource> updateProfile(UserPatchResource userPatchResource) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel existingUser = userDaoService.findOneBy(UserSpecification.withEmail(email));
        existingUser = userMapper.patchResourceToModel(userPatchResource,existingUser);
        existingUser = userDaoService.save(existingUser);
        log.info("user updated successfully with id: {}", existingUser.getId());
        return ResponseEntity.ok(userMapper.modelToGetResource(existingUser));
    }

}
