package com.getyourticket.cf.service.exceptions;

import java.io.Serial;

public class UserAlreadyExistsExceptions extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsExceptions(String username) {
        super("User with username: " + username + " already exists");
    }
}
