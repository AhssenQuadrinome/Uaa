package com.ourbusway.uaa.dao.specification;

import com.ourbusway.uaa.enumeration.TokenTypeEnum;
import com.ourbusway.uaa.model.TokenModel;
import com.ourbusway.uaa.model.TokenModel_;
import org.springframework.data.jpa.domain.Specification;

public class TokenSpecification {
    public static Specification<TokenModel> withEncryptedToken(String token) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(TokenModel_.value), token);
    }

    public static Specification<TokenModel> withType(TokenTypeEnum tokenType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(TokenModel_.type), tokenType);
    }
}
