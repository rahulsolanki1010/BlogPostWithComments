package com.example.blogpost.auth.security;

import com.example.blogpost.exception.ApplicationException;
import com.example.blogpost.user.dto.UserDTO;
import com.example.blogpost.user.dto.UserSessionInfo;
import com.example.blogpost.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.example.blogpost.common.constant.CmnConstants.USER_SESSION_INFO;

@Component
@EnableWebSecurity
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtServices;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Creating UserSessionInfo object with necessary fields which can used in further business logic
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        userSessionInfo.setRemoteHostName(request.getRemoteHost());
        userSessionInfo.setRemoteIpAddr(request.getRemoteAddr());
        userSessionInfo.setUserDTO(new UserDTO());
        request.setAttribute(USER_SESSION_INFO, userSessionInfo);
        String token = request.getHeader("Authorization");
        if(token!=null && token.startsWith("Bearer")){
            token = token.substring(7);
            if(token!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                User userDetails = jwtServices.getUserDetailsFromToken(token).orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
                UserDTO userDTO = new UserDTO();
                userDTO.setId(userDetails.getId());
                userDTO.setEmail(userDetails.getEmailId());
                userDTO.setUsername(userDetails.getUsername());
                userDTO.setAboutMe(userDetails.getAboutMe());
                userSessionInfo.setUserDTO(userDTO);
                if(jwtServices.validateToken(token)){
                    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(userToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
