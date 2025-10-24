package com.ourbusway.uaa.model;

import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.model.base.AbstractUuidBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "N_USER")
public class UserModel extends AbstractUuidBaseModel {
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;
    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "DATE_OF_BIRTH")
    private String dateOfBirth;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ENCRYPTED_PASSWORD")
    private String password;


    @Column(name = "IS_ACTIVATED")
    private boolean activated = false;

    @Column(name = "IS_ENABLED")
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

}
