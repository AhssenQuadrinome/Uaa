package com.ourbusway.uaa.service;

import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.UserModel;

public interface TokenService {

    TokenModel generatePasswordToken(UserModel user);

    TokenModel generateAccountValidationToken(UserModel user);
}
