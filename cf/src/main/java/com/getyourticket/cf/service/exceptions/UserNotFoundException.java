package com.getyourticket.cf.service.exceptions;

import java.io.Serial;

public class UserNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
