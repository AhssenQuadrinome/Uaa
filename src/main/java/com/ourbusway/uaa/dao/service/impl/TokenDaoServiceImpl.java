package com.ourbusway.uaa.dao.service.impl;

import com.ourbusway.uaa.dao.repository.TokenRepository;
import com.ourbusway.uaa.dao.service.TokenDaoService;
import com.ourbusway.uaa.exception.ResourceNotFoundException;
import com.ourbusway.uaa.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.ourbusway.uaa.model.TokenModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenDaoServiceImpl implements TokenDaoService {

    private final TokenRepository tokenRepository;

    @Override
    public TokenModel findOneBy(Specification<TokenModel> specification) {
        return tokenRepository
                .findOne(specification)
                .orElseThrow(
                        () -> {
                            log.debug("Couldn't find any token with the specified criteria");
                            return new ResourceNotFoundException(
                                    ResourceNotFoundExceptionTitleEnum.USER_NOT_FOUND,
                                    "No token found with the specified criteria");
                        });
    }

    @Override
    public TokenModel save(TokenModel tokenModel) {
        return tokenRepository.save(tokenModel);
    }

    @Override
    public void delete(TokenModel tokenModel) {
        tokenRepository.delete(tokenModel);
    }
}
