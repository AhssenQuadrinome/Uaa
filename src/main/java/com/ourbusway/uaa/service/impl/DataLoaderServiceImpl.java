package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.dao.repository.UserRepository;
import com.ourbusway.uaa.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderServiceImpl implements DataLoaderService, CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDaoService userDaoService;

    @Override
    public void createAdminUser() {
        final String adminEmail = "ourbusway2025@outlook.com";

        if (!userDaoService.existsBy(UserSpecification.withEmail(adminEmail))) {
            log.info("Admin user <{}> not found. Creating default admin...", adminEmail);

            UserModel admin = new UserModel();
            admin.setFirstName("OurBusWay Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setActivated(true);
            admin.setEnabled(true);
            admin.setRole(RoleEnum.ADMINISTRATOR);

            userDaoService.save(admin);
            log.info("Admin user created successfully with email: {}", adminEmail);
        } else {
            log.info("Admin user <{}> already exists", adminEmail);
        }
    }

    @Override
    public void run(String... args) {
        this.createAdminUser();
    }
}
