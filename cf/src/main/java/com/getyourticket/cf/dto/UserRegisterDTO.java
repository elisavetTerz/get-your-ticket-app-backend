package com.getyourticket.cf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String username;
}
