package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.mappers.UserMapper;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import com.ourbusway.uaa.service.AdminService;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserDaoService userDaoService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final MailingService mailingService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<UserGetResource> createUserAsAdmin(UserRegistrationPostResource registrationRequest) {
        log.debug("Admin creating user with email: {}", registrationRequest.getEmail());

        // Validate admin can only create specific roles
        if (registrationRequest.getRole() == null ||
                (registrationRequest.getRole() != RoleEnum.DRIVER && registrationRequest.getRole() != RoleEnum.CONTROLLER)) {
            log.warn("Admin attempted to create user with invalid role: {}", registrationRequest.getRole());
            throw new IllegalArgumentException("Admin can only create DRIVER or CONTROLLER accounts");
        }

        // Check email uniqueness
        if (userDaoService.existsBy(UserSpecification.withEmail(registrationRequest.getEmail()))) {
            log.warn("Admin creation failed: email {} already exists", registrationRequest.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        // Create user with admin-specified role
        UserModel user = userMapper.registrationToModel(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole());
        user.setActivated(false);
        user.setEnabled(true);

        user = userDaoService.save(user);
        log.info("Admin created user successfully with email {} and role {}", user.getEmail(), user.getRole());

        // Send activation email
        TokenModel tokenModel = tokenService.generateAccountValidationToken(user);
        mailingService.sendActivationAccountEmail(user, tokenModel);
        log.debug("Activation email sent to {}", user.getEmail());

        return ResponseEntity.ok(userMapper.modelToGetResource(user));
    }

    @Override
    public ResponseEntity<Page<UserGetResource>> getAllUsers(Pageable pageable) {
        log.debug("Admin fetching all users with pagination: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());

        // Create specification to exclude ADMINISTRATOR role
        Specification<UserModel> excludeAdminsSpec = (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("role"), RoleEnum.ADMINISTRATOR);

        Page<UserModel> users = userDaoService.findAllBy(excludeAdminsSpec, pageable);
        Page<UserGetResource> userResources = users.map(userMapper::modelToGetResource);

        log.info("Admin retrieved {} users", userResources.getTotalElements());
        return ResponseEntity.ok(userResources);
    }
}