package com.ourbusway.uaa.dao.service;

import com.ourbusway.uaa.model.TokenModel;
import org.springframework.data.jpa.domain.Specification;

public interface TokenDaoService {

    TokenModel findOneBy(Specification<TokenModel> specification);

    TokenModel save(TokenModel tokenModel);

    void delete(TokenModel tokenModel);
}
