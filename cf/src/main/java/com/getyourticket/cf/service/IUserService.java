package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.service.exceptions.UserAlreadyExistsExceptions;
import com.getyourticket.cf.service.exceptions.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IUserService {
    User insertUser(UserRegisterDTO userRegisterDTO) throws UserAlreadyExistsExceptions;
    User updateUser(User user) throws EntityNotFoundException, UserNotFoundException;
    User deleteUser(Integer id) throws EntityNotFoundException, UserNotFoundException;
    List<User> getUsersByLastname(String lastname) throws EntityNotFoundException;
    User getUserById(Integer id) throws EntityNotFoundException, UserNotFoundException;
    User loginUser(String username, String password) throws UserNotFoundException, Exception;
}
