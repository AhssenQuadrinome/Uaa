package com.ourbusway.uaa.dao.service;

import com.ourbusway.uaa.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserDaoService {

    UserModel findOneBy(Specification<UserModel> specification);

    UserModel save(UserModel loginModel);

    boolean existsBy(Specification<UserModel> specification);

    Page<UserModel> findAllBy(Specification<UserModel> specification, Pageable pageable);

    UserModel createUser(UserModel userModel, String role);

    Page<UserModel> getAllUsers(Pageable pageable);

    UserModel getProfile(String email);
}
