package com.ourbusway.uaa.controller;

import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/register")
    ResponseEntity<UserGetResource> register(@Valid @RequestBody UserRegistrationPostResource userRegistrationPostResource);

    @PostMapping("/admin/create")
    ResponseEntity<UserGetResource> createUserAsAdmin(@Valid @RequestBody UserRegistrationPostResource userRegistrationPostResource);

    @GetMapping("/profile")
    ResponseEntity<UserGetResource> getProfile();

    @PatchMapping("/profile")
    ResponseEntity<UserGetResource> updateProfile(@Valid @RequestBody UserPatchResource userPatchResource);
}