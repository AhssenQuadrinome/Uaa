package com.ourbusway.uaa.resource.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCredentialsPostResource {

    @NotEmpty
    private String username;
    @ToString.Exclude
    @NotEmpty
    private String password;
}
