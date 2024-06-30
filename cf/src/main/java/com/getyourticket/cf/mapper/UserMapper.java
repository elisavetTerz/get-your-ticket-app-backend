package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;

public interface UserMapper {
    User toEntity(UserRegisterDTO userRegisterDTO);

    UserRegisterDTO toDTO(User user);
}