package com.momand.docauthor.jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(this.username.equals(username)){
            return new User(username, password, new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("Username not Found!");
        }
    }
}
