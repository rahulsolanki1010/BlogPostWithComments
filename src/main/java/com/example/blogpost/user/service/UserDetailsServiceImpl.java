package com.example.blogpost.user.service;

import com.example.blogpost.user.model.User;
import com.example.blogpost.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public ResponseEntity<Object> registerUser(User user) {
        User savedUser = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("user registered successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
}
