package com.ourbusway.uaa.dao.repository;

import com.ourbusway.uaa.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends JpaRepository<UserModel, String>, JpaSpecificationExecutor<UserModel> {
}
