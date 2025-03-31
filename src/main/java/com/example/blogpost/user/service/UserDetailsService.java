package com.example.blogpost.user.service;

import com.example.blogpost.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    UserDetails getUserByUsername(String username);

    ResponseEntity<Object> registerUser(User user);
}
