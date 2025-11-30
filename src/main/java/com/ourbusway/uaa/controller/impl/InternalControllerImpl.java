package com.ourbusway.uaa.controller.impl;

import com.ourbusway.uaa.controller.InternalController;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InternalControllerImpl implements InternalController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserGetResource> getUserInternal(String id){
        return userService.getUserById(id);
    };

}
