package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.UserController;
import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import com.ourbusway.uaa.service.MailingService;
import com.ourbusway.uaa.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserDaoService userDaoService;
    private final TokenService tokenService;
    private final MailingService mailingService;

    // Passenger registration
    @Override
    @PostMapping("/register")
    public ResponseEntity<UserGetResource> register(
            @Valid @RequestBody UserRegistrationPostResource resource) {

        UserModel user = new UserModel();
        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setEmail(resource.getEmail());
        user.setPassword(resource.getPassword()); // hashed in DAO
        user.setMobile(resource.getMobile());
        user.setDateOfBirth(resource.getDateOfBirth());
        user.setGender(resource.getGender());
        user.setAddress(resource.getAddress());
        user.setRole(RoleEnum.PASSENGER);
        user.setEnabled(true);

        UserModel savedUser = userDaoService.createUser(user, "PASSENGER");

        var token = tokenService.generateAccountValidationToken(savedUser);
        mailingService.sendActivationAccountEmail(savedUser, token);

        return ResponseEntity.ok(toGetResource(savedUser));
    }

    // Admin creating driver/controller
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserGetResource> createUserAsAdmin(
            @Valid @RequestBody UserRegistrationPostResource resource) {

        if (resource.getRole() != RoleEnum.DRIVER && resource.getRole() != RoleEnum.CONTROLLER) {
            return ResponseEntity.badRequest().body(null);
        }

        UserModel user = new UserModel();
        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setEmail(resource.getEmail());
        user.setPassword(resource.getPassword()); // hashed in DAO
        user.setMobile(resource.getMobile());
        user.setDateOfBirth(resource.getDateOfBirth());
        user.setGender(resource.getGender());
        user.setAddress(resource.getAddress());
        user.setRole(resource.getRole());
        user.setEnabled(true);

        UserModel savedUser = userDaoService.createUser(user, resource.getRole().name());

        var token = tokenService.generateAccountValidationToken(savedUser);
        mailingService.sendActivationAccountEmail(savedUser, token);

        return ResponseEntity.ok(toGetResource(savedUser));
    }

    // Profile endpoints
    @Override
    @GetMapping("/profile")
    public ResponseEntity<UserGetResource> getProfile(Principal principal) {
        UserModel user = userDaoService.getProfile(principal.getName());
        return ResponseEntity.ok(toGetResource(user));
    }

    @Override
    @PatchMapping("/profile")
    public ResponseEntity<UserGetResource> updateProfile(
            @Valid @RequestBody UserPatchResource resource, Principal principal) {

        UserModel user = userDaoService.getProfile(principal.getName());

        if (resource.getFirstName() != null) user.setFirstName(resource.getFirstName());
        if (resource.getLastName() != null) user.setLastName(resource.getLastName());
        if (resource.getEmail() != null) user.setEmail(resource.getEmail());
        if (resource.getMobile() != null) user.setMobile(resource.getMobile());
        if (resource.getDateOfBirth() != null) user.setDateOfBirth(resource.getDateOfBirth());
        if (resource.getGender() != null) user.setGender(resource.getGender());
        if (resource.getAddress() != null) user.setAddress(resource.getAddress());

        UserModel updatedUser = userDaoService.save(user);

        return ResponseEntity.ok(toGetResource(updatedUser));
    }

    // Helper: User → DTO
    private UserGetResource toGetResource(UserModel model) {
        return UserGetResource.builder()
                .id(model.getId())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .email(model.getEmail())
                .mobile(model.getMobile())
                .dateOfBirth(model.getDateOfBirth())
                .gender(model.getGender())
                .address(model.getAddress())
                .role(model.getRole())
                .enabled(model.isEnabled())
                .build();
    }
}
