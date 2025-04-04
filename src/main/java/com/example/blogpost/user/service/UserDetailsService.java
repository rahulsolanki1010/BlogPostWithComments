package com.example.blogpost.user.service;

import com.example.blogpost.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserDetailsService{
    Optional<User> getUserByUsername(String username);

    ResponseEntity<Object> registerUser(User user);
}
