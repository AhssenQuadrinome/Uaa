package com.ourbusway.uaa.service;

import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<UserGetResource> createUserAsAdmin(UserRegistrationPostResource userRegistrationPostResource);

    ResponseEntity<Page<UserGetResource>> getAllUsers(Pageable pageable);
}