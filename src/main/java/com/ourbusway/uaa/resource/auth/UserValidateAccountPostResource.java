package com.ourbusway.uaa.resource.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserValidateAccountPostResource {
    @NotEmpty
    private String activationKey;
    private String password;
}
