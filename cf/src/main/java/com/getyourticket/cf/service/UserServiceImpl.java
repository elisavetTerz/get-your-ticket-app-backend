package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.repository.UserRepository;
import com.getyourticket.cf.mapper.UserMapper;
import com.getyourticket.cf.service.exceptions.UserAlreadyExistsExceptions;
import com.getyourticket.cf.service.exceptions.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User insertUser(UserRegisterDTO userRegisterDTO) throws UserAlreadyExistsExceptions {
        if (userRepository.findByEmailAndPassword(userRegisterDTO.getEmail(), userRegisterDTO.getPassword()).isPresent()) {
            throw new UserAlreadyExistsExceptions(userRegisterDTO.getUsername());
        }
        User user = userMapper.toEntity(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(User user) throws UserNotFoundException {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException(user.getUsername());
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User deleteUser(Integer id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public List<User> getUsersByLastname(String lastname) throws EntityNotFoundException {
        List<User> users = userRepository.findByLastnameStartingWith(lastname);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return users;
    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return userRepository.findUserById(id);
    }

    @Override
    public User loginUser(String username, String password) throws UserNotFoundException, Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new Exception("Invalid credentials");
        }
    }
}
