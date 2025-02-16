package com.example.spendiq.mapper;

import com.example.spendiq.dto.user.UserRequestDTO;
import com.example.spendiq.dto.user.UserResponseDTO;
import com.example.spendiq.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(User user);
    User toUser(UserRequestDTO requestDTO);
}
