package com.ourbusway.uaa.validator.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.util.PathVariableHelper;
import com.ourbusway.uaa.validator.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class UniqueEmailImpl implements ConstraintValidator<UniqueEmail, String> {

    private final UserDaoService userDaoService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // if user email is empty
        if (ObjectUtils.isEmpty(email)) {
            return true;
        } else {
            // user creation
            if (Boolean.FALSE.equals(userDaoService.existsBy(UserSpecification.withEmail(email)))) {
                return true;
            }

            // user update
            String userId = PathVariableHelper.getPathVariable("userId");
            UserModel userModel = userDaoService.findOneBy(UserSpecification.withUuid(userId));
            return userModel != null && userModel.getEmail().equals(email);
        }
    }
}
