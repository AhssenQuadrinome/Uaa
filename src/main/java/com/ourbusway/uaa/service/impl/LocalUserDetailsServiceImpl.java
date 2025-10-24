package com.ourbusway.uaa.service.impl;

import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.dao.specification.UserSpecification;
import com.ourbusway.uaa.enumeration.RoleEnum;
import com.ourbusway.uaa.exception.AuthorizationForbiddenException;
import com.ourbusway.uaa.exception.ResourceNotFoundException;
import com.ourbusway.uaa.exception.enumeration.AuthorizationForbiddenExceptionTitleEnum;
import com.ourbusway.uaa.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.service.LocalUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalUserDetailsServiceImpl implements LocalUserDetailsService {

    private final UserDaoService userDaoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserModel userModel = userDaoService.findOneBy(UserSpecification.withEmail(username));
            return createSpringSecurityUser(userModel);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.USER_NOT_FOUND,
                    "User with email " + username + " was not found in the database"
            );
        }
    }

    private UserDetails createSpringSecurityUser(UserModel userModel) {
        if (!userModel.isEnabled()) {
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.USER_DISABLED,
                    "Your account is disabled. Please contact support."
            );
        }
        if (!userModel.isActivated()) {
            throw new AuthorizationForbiddenException(
                    AuthorizationForbiddenExceptionTitleEnum.USER_NOT_ACTIVATED,
                    "Your account is not yet validated. Please check your email for validation instructions."
            );
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userModel.getRole().name());
        return new User(userModel.getEmail(), userModel.getPassword(), java.util.List.of(authority));
    }
}
