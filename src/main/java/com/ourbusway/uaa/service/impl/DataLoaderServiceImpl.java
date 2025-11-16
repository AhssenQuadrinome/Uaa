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
    public void createDefaultPassenger() {
        final String passengerEmail = "passenger@ourbusway.com";

        if (!userDaoService.existsBy(UserSpecification.withEmail(passengerEmail))) {
            log.info("Default passenger <{}> not found. Creating...", passengerEmail);

            UserModel passenger = new UserModel();
            passenger.setFirstName("Default");
            passenger.setLastName("Passenger");
            passenger.setEmail(passengerEmail);
            passenger.setPassword(passwordEncoder.encode("passenger123"));
            passenger.setMobile("+212611111111");
            passenger.setDateOfBirth("1990-01-01");
            passenger.setGender("Male");
            passenger.setAddress("Casablanca, Morocco");
            passenger.setActivated(true);
            passenger.setEnabled(true);
            passenger.setRole(RoleEnum.PASSENGER);

            userDaoService.save(passenger);
            log.info("Default passenger created successfully with email: {}", passengerEmail);
        } else {
            log.info("Default passenger <{}> already exists", passengerEmail);
        }
    }

    @Override
    public void createDefaultDriver() {
        final String driverEmail = "driver@ourbusway.com";

        if (!userDaoService.existsBy(UserSpecification.withEmail(driverEmail))) {
            log.info("Default driver <{}> not found. Creating...", driverEmail);

            UserModel driver = new UserModel();
            driver.setFirstName("Default");
            driver.setLastName("Driver");
            driver.setEmail(driverEmail);
            driver.setPassword(passwordEncoder.encode("driver123"));
            driver.setMobile("+212622222222");
            driver.setDateOfBirth("1985-05-15");
            driver.setGender("Male");
            driver.setAddress("Rabat, Morocco");
            driver.setActivated(true);
            driver.setEnabled(true);
            driver.setRole(RoleEnum.DRIVER);

            userDaoService.save(driver);
            log.info("Default driver created successfully with email: {}", driverEmail);
        } else {
            log.info("Default driver <{}> already exists", driverEmail);
        }
    }

    @Override
    public void createDefaultController() {
        final String controllerEmail = "controller@ourbusway.com";

        if (!userDaoService.existsBy(UserSpecification.withEmail(controllerEmail))) {
            log.info("Default controller <{}> not found. Creating...", controllerEmail);

            UserModel controller = new UserModel();
            controller.setFirstName("Default");
            controller.setLastName("Controller");
            controller.setEmail(controllerEmail);
            controller.setPassword(passwordEncoder.encode("controller123"));
            controller.setMobile("+212633333333");
            controller.setDateOfBirth("1988-08-20");
            controller.setGender("Female");
            controller.setAddress("Rabat, Morocco");
            controller.setActivated(true);
            controller.setEnabled(true);
            controller.setRole(RoleEnum.CONTROLLER);

            userDaoService.save(controller);
            log.info("Default controller created successfully with email: {}", controllerEmail);
        } else {
            log.info("Default controller <{}> already exists", controllerEmail);
        }
    }

    @Override
    public void run(String... args) {
        this.createAdminUser();
        this.createDefaultPassenger();
        this.createDefaultDriver();
        this.createDefaultController();

        log.info("Default users creation completed!");
    }
}
