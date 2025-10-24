package com.ourbusway.uaa.dao.specification;

import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.model.UserModel_;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserModel> withUuid(String uuid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.ID), uuid);
    }

    public static Specification<UserModel> withEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.EMAIL), email);
    }

    public static Specification<UserModel> withEnabled(boolean value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.ENABLED), value);
    }

    public static Specification<UserModel> withActive(boolean value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.ACTIVATED), value);
    }

    public static Specification<UserModel> withEmailLike(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(UserModel_.EMAIL), "%" + search + "%");
    }

    public static Specification<UserModel> withFirstNameLike(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(UserModel_.FIRST_NAME), "%" + search + "%");
    }
    
}
