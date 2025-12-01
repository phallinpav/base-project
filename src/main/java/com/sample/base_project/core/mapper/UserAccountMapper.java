package com.sample.base_project.core.mapper;

import com.sample.base_project.core.constants.UserStatus;
import com.sample.base_project.core.constants.base.BaseEnum;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.request.CreateUserRequest;
import com.sample.base_project.core.request.UpdateUserRequest;
import com.sample.base_project.core.response.UserAccountDto;
import com.sample.base_project.core.response.UserDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { EnumMapper.class, AccountMapper.class })
public interface UserAccountMapper {
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    @Mappings({
            @Mapping(source = ".", target = "fullName", qualifiedByName = "toFullName"),
            @Mapping(source = "status", target = "status", qualifiedByName = "toEnumDto")
    })
    UserAccountDto toDto(User user, @Context Class<UserStatus> clazz);

    default UserAccountDto toDto(User user) {
        return toDto(user, UserStatus.class);
    }

    @Named("toFullName")
    default String toFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
