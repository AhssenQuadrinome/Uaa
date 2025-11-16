package com.ourbusway.uaa.service;

import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    ResponseEntity<UserGetResource> register(UserRegistrationPostResource userRegistrationPostResource);

    ResponseEntity<UserGetResource> createUserAsAdmin(UserRegistrationPostResource userRegistrationPostResource);

    ResponseEntity<UserGetResource> getProfile();

    ResponseEntity<UserGetResource> updateProfile(UserPatchResource userPatchResource);
}