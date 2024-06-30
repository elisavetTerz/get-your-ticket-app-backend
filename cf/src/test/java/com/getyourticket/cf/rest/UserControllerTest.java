package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.service.IUserService;
import com.getyourticket.cf.validator.UserRegisterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private UserRegisterValidator userRegisterValidator;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        WebBindingInitializer initializer = new WebBindingInitializer() {
            @Override
            public void initBinder(WebDataBinder binder) {
                if (binder.getTarget() instanceof UserRegisterDTO) {
                    binder.setValidator(userRegisterValidator);
                }
            }
        };

        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setCustomArgumentResolvers(new CustomArgumentResolver(initializer))
                .build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setUsername("testuser");

        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");

        when(userRegisterValidator.supports(any())).thenReturn(true);

        when(userService.insertUser(any(UserRegisterDTO.class))).thenReturn(user);
        doNothing().when(userRegisterValidator).validate(any(Object.class), any(BindingResult.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"username\":\"testuser\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testRegisterUserInvalidInput() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("invalid-email");  // Set an invalid email
        userRegisterDTO.setPassword("short");       // Set a short password
        userRegisterDTO.setUsername("te");          // Set a short username

        // Mock that the validator supports UserRegisterDTO
        when(userRegisterValidator.supports(any(Class.class))).thenAnswer(invocation -> {
            Class<?> clazz = invocation.getArgument(0);
            return UserRegisterDTO.class.equals(clazz);
        });

        // Mock the validate method to populate errors in the BindingResult
        doAnswer(invocation -> {
            Object target = invocation.getArgument(0);
            Errors errors = invocation.getArgument(1);

            if (target instanceof UserRegisterDTO) {
                UserRegisterDTO dto = (UserRegisterDTO) target;

                // Simulate validation errors
                if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    errors.rejectValue("email", "format", "Invalid email format");
                }
                if (dto.getPassword().length() < 6) {
                    errors.rejectValue("password", "size", "Password should be at least 6 characters long");
                }
                if (dto.getUsername().length() < 3 || dto.getUsername().length() > 50) {
                    errors.rejectValue("username", "size", "Username should be between 3 and 50 characters");
                }
            }
            return null;
        }).when(userRegisterValidator).validate(any(Object.class), any(Errors.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\",\"password\":\"short\",\"username\":\"te\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].code").value("format"))
                .andExpect(jsonPath("$[1].code").value("size"))
                .andExpect(jsonPath("$[2].code").value("size"));
    }
}
