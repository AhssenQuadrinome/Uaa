package com.ourbusway.uaa.controller;

import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
public interface AdminController {

    @PostMapping("/create")
    ResponseEntity<UserGetResource> createUserAsAdmin(@Valid @RequestBody UserRegistrationPostResource userRegistrationPostResource);

    @GetMapping("/users")
    ResponseEntity<Page<UserGetResource>> getAllUsers(Pageable pageable);

    @PatchMapping("/users/{userId}/toggle-status")
    ResponseEntity<UserGetResource> toggleUserStatus(@PathVariable String userId);
}