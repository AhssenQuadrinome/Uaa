package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.AdminController;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import com.ourbusway.uaa.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public ResponseEntity<UserGetResource> createUserAsAdmin(@Valid @RequestBody UserRegistrationPostResource userRegistrationPostResource) {
        return adminService.createUserAsAdmin(userRegistrationPostResource);
    }

    @Override
    public ResponseEntity<Page<UserGetResource>> getAllUsers(Pageable pageable) {
        return adminService.getAllUsers(pageable);
    }
}