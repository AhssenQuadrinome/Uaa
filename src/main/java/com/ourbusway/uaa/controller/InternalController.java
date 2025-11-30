package com.ourbusway.uaa.controller;

import com.ourbusway.uaa.resource.user.UserGetResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/internal")
public interface InternalController {

    @GetMapping("/users/{id}")
    ResponseEntity<UserGetResource> getUserInternal(@PathVariable String id);

}
