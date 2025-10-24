package com.ourbusway.uaa.dao.repository;

import com.ourbusway.uaa.model.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository
        extends JpaRepository<TokenModel, String>, JpaSpecificationExecutor<TokenModel> {
}
