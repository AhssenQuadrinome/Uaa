package com.ourbusway.uaa.dao.specification;

import com.ourbusway.uaa.enumeration.RoleEnum;
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

    public static Specification<UserModel> withRole(RoleEnum role) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.ROLE), role);
    }

    public static Specification<UserModel> withLastNameLike(String search) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(UserModel_.LAST_NAME), "%" + search + "%");
    }

    public static Specification<UserModel> withFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.FIRST_NAME), firstName);
    }

    public static Specification<UserModel> withLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.LAST_NAME), lastName);
    }

    public static Specification<UserModel> withMobile(String mobile) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserModel_.MOBILE), mobile);
    }

    public static Specification<UserModel> searchByNameOrEmail(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(UserModel_.FIRST_NAME)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(UserModel_.LAST_NAME)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(UserModel_.EMAIL)), likePattern)
            );
        };
    }
}
