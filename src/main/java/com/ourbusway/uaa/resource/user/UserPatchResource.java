package com.ourbusway.uaa.resource.user;

import com.ourbusway.uaa.validator.UniqueEmail;
import lombok.Data;



@Data
public class UserPatchResource {
    private String firstName;
    private String lastName;
    @UniqueEmail
    private String email;
    private String mobile;
    private String dateOfBirth;
    private String gender;
    private String address;
}
