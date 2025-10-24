package com.ourbusway.uaa.resource.user;

import com.ourbusway.uaa.enumeration.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGetResource {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

    private String dateOfBirth;
    private String gender;
    private String address;

    private RoleEnum role;
    private boolean enabled;
}
