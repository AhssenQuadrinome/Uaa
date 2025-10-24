package com.ourbusway.uaa.model;

import com.ourbusway.uaa.enumeration.TokenTypeEnum;
import com.ourbusway.uaa.model.base.AbstractUuidBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "N_TOKEN")
public class TokenModel extends AbstractUuidBaseModel {

    @Column(name = "ENCRYPTED_VALUE", length = 500)
    private String value;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private TokenTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserModel login;

}

