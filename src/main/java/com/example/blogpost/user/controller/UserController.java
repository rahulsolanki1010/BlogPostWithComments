package com.example.blogpost.user.controller;

import com.example.blogpost.auth.security.JWTService;
import com.example.blogpost.exception.ApplicationException;
import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.user.dto.LoginDto;
import com.example.blogpost.user.model.User;
import com.example.blogpost.user.service.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;
    @PostMapping("sign-up")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDetailsService.registerUser(user);
    }

    @PostMapping("login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginDto loginDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //String username = myUserDetailsService.findByUsername(loginDto.getUsername()).getUsername();
        return  jwtService.generateToken(loginDto.getUsername());
    }
}
