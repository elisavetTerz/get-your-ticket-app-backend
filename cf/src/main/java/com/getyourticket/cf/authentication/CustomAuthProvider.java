package com.getyourticket.cf.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomAuthProvider {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(11); //default is 10 === 2^10 iterations
        return NoOpPasswordEncoder.getInstance();

    }
}
