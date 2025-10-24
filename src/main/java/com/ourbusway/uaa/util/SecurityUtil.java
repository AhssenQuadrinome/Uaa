package com.ourbusway.uaa.util;

import com.ourbusway.uaa.model.UserModel;

import java.util.UUID;

public class SecurityUtil {

    public static final String TOKEN_TYPE = "Bearer";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String AUTHORITIES_KEY = "authorities";

    public static final String USERNAME_KEY = "username";

    public static final String IDENTIFIER_KEY = "identifier";

    public static final String SYSTEM_ACCOUNT = "SYSTEM";

    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String removeRolePrefix(String role) {
        return role.replace(ROLE_PREFIX, "");
    }

    public static boolean isAdmin(UserModel user) {
        return user != null && user.getRole() != null
                && user.getRole().name().equalsIgnoreCase("ADMINISTRATOR");
    }

}
