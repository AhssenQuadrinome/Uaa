package com.ourbusway.uaa.resource.user;

import com.ourbusway.uaa.enumeration.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationPostResource {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String mobile;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String dateOfBirth;
    private String gender;
    private String address;

    private RoleEnum role;
}
