package com.re.cinemabookingapp.mapper;

import com.re.cinemabookingapp.dto.auth.UserInfoResponseDto;
import com.re.cinemabookingapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", source = "userProfile.fullName")
    @Mapping(target = "email", source = "userProfile.email")
    UserInfoResponseDto toDto(User user);
}
