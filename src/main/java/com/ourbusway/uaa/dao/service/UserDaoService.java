package com.ourbusway.uaa.dao.service;

import com.ourbusway.uaa.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface UserDaoService {

    UserModel findOneBy(Specification<UserModel> specification);

    Optional<UserModel> findById(String id);

    UserModel save(UserModel userModel);

    boolean existsBy(Specification<UserModel> specification);

    Page<UserModel> findAllBy(Specification<UserModel> specification, Pageable pageable);
}