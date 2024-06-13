package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            return null;
        }

        User user = new User();
        user.setFirstname(userRegisterDTO.getFirstname());
        user.setLastname(userRegisterDTO.getLastname());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setUsername(userRegisterDTO.getUsername());

        return user;
    }

    @Override
    public UserRegisterDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setFirstname(user.getFirstname());
        userRegisterDTO.setLastname(user.getLastname());
        userRegisterDTO.setEmail(user.getEmail());
        userRegisterDTO.setPassword(user.getPassword());
        userRegisterDTO.setUsername(user.getUsername());

        return userRegisterDTO;
    }
}
