package com.ourbusway.uaa.mappers;

import com.ourbusway.uaa.model.UserModel;
import com.ourbusway.uaa.resource.user.UserGetResource;
import com.ourbusway.uaa.resource.user.UserPatchResource;
import com.ourbusway.uaa.resource.user.UserRegistrationPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;

@Slf4j
@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    public abstract UserGetResource modelToGetResource(UserModel userModel);

    public abstract UserModel registrationToModel(UserRegistrationPostResource registrationRequest);

    public abstract UserModel patchResourceToModel(UserPatchResource userPatchResource, @MappingTarget UserModel existingUser);

}
