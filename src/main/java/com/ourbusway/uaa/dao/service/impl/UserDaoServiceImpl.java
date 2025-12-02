package com.ourbusway.uaa.dao.service.impl;

import com.ourbusway.uaa.dao.repository.UserRepository;
import com.ourbusway.uaa.dao.service.UserDaoService;
import com.ourbusway.uaa.exception.ResourceNotFoundException;
import com.ourbusway.uaa.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.ourbusway.uaa.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDaoServiceImpl implements UserDaoService {
    private final UserRepository userRepository;

    @Override
    public UserModel findOneBy(Specification<UserModel> specification) {
        return userRepository
                .findOne(specification)
                .orElseThrow(() -> {
                    log.debug("Couldn't find any user with the specified criteria");
                    return new ResourceNotFoundException(
                            ResourceNotFoundExceptionTitleEnum.USER_NOT_FOUND,
                            "No user found with the specified criteria");
                });
    }

    @Override
    public Optional<UserModel> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsBy(Specification<UserModel> specification) {
        return userRepository.exists(specification);
    }

    @Override
    public Page<UserModel> findAllBy(Specification<UserModel> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }
}