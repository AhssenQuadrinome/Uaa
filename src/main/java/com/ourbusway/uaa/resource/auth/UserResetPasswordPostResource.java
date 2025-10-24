package com.ourbusway.uaa.resource.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserResetPasswordPostResource {
    @NotEmpty
    private String resetPasswordKey;

    @NotEmpty
    private String password;
}
