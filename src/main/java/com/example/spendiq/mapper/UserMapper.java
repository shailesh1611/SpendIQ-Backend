package com.example.spendiq.mapper;

import com.example.spendiq.dto.user.UserRequestDTO;
import com.example.spendiq.dto.user.UserResponseDTO;
import com.example.spendiq.entity.Role;
import com.example.spendiq.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoles")
    UserResponseDTO toUserResponseDTO(User user);
    User toUser(UserRequestDTO requestDTO);

    @Named(value = "mapRoles")
    default List<String> qualifier(User user) {
        return user.getRoles().stream().map(Role::getRoleName).toList();
    }
}
