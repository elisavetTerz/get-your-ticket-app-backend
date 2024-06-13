package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.dto.UserLoginDTO;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.service.IUserService;
import com.getyourticket.cf.validator.UserRegisterValidator;
import com.getyourticket.cf.validator.UserLoginValidator;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4500")
public class UserController {

    private final IUserService userService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserLoginValidator userLoginValidator;

    @InitBinder("userRegisterDTO")
    protected void initRegisterBinder(WebDataBinder binder) {
        binder.setValidator(userRegisterValidator);
    }

    @InitBinder("userLoginDTO")
    protected void initLoginBinder(WebDataBinder binder) {
        binder.setValidator(userLoginValidator);
    }


    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisterDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            //return ResponseEntity.badRequest().body(new ApiResponse("Validation errors occurred"));
        }

        try {
            User user = userService.insertUser(userRegisterDTO);
            return ResponseEntity.ok(user);
            //            return ResponseEntity.status(201).body(new ApiResponseDTO("User registered"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
//            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
//                return ResponseEntity.status(400).body(new ApiResponseDTO("Email already in use"));
//            }
//            return ResponseEntity.status(400).body(new ApiResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserLoginDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            User user = userService.loginUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
