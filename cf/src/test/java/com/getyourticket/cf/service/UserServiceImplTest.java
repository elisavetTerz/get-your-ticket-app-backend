package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.repository.UserRepository;
import com.getyourticket.cf.mapper.UserMapper;
import com.getyourticket.cf.service.exceptions.UserAlreadyExistsExceptions;
import com.getyourticket.cf.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    public void setup() {
        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setUsername("testuser");
    }

    @Test
    public void testInsertUser_Success() throws UserAlreadyExistsExceptions {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmailAndPassword("test@example.com", "password")).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(UserRegisterDTO.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User insertedUser = userService.insertUser(userRegisterDTO);

        assertNotNull(insertedUser);
        assertEquals("test@example.com", insertedUser.getEmail());
        assertEquals("encodedPassword", insertedUser.getPassword());

        verify(userRepository, times(1)).findByEmailAndPassword("test@example.com", "password");
        verify(userMapper, times(1)).toEntity(userRegisterDTO);
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testInsertUser_UserAlreadyExists() {
        when(userRepository.findByEmailAndPassword("test@example.com", "password")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsExceptions.class, () -> {
            userService.insertUser(userRegisterDTO);
        });

        verify(userRepository, times(1)).findByEmailAndPassword("test@example.com", "password");
        verifyNoMoreInteractions(userMapper, passwordEncoder, userRepository);
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getId());
        assertEquals("testuser", updatedUser.getUsername());

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(user);
        });

        verify(userRepository, times(1)).existsById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDeleteUser_Success() throws UserNotFoundException {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUser(1);

        assertNotNull(deletedUser);
        assertEquals(1, deletedUser.getId());
        assertEquals("testuser", deletedUser.getUsername());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1);
        });

        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUserById_Success() throws UserNotFoundException {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findUserById(1)).thenReturn(user);

        User foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).findUserById(1);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1);
        });

        verify(userRepository, times(1)).existsById(1);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testLoginUser_Success() throws UserNotFoundException, Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User loggedInUser = userService.loginUser("testuser", "password");

        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());
        assertEquals("encodedPassword", loggedInUser.getPassword());

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
    }

    @Test
    public void testLoginUser_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.loginUser("testuser", "password");
        });

        verify(userRepository, times(1)).findByUsername("testuser");
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(Exception.class, () -> {
            userService.loginUser("testuser", "password");
        });

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
    }
}
