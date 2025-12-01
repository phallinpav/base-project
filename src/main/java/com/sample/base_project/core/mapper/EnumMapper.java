package com.sample.base_project.core.mapper;

import com.sample.base_project.core.constants.base.BaseEnum;
import com.sample.base_project.core.response.base.EnumDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EnumMapper {
    @Named("toEnumDto")
    default <T extends BaseEnum> EnumDto toEnumDto(String value, @Context Class<T> clazz) {
        return EnumDto.of(BaseEnum.fromValue(clazz, value));
    }
}
