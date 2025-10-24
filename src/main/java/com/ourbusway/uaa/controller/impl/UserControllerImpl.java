package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.UserController;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import com.ourbusway.uaa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserGetResource> register(
            UserRegistrationPostResource userRegistrationPostResource){
        return userService.register(userRegistrationPostResource);
    }

    @Override
    public ResponseEntity<UserGetResource> getProfile() {
        return userService.getProfile();
    }

    @Override
    public ResponseEntity<UserGetResource> updateProfile(UserPatchResource userPatchResource){
        return userService.updateProfile(userPatchResource);
    }
}
