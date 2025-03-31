package com.example.blogpost.auth.security;

import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.response.TokenResponse;
import com.example.blogpost.user.service.UserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTService {
    public final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B625064536756685970";

    private Key getsigningkey() {
        byte[] ketBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(ketBytes);
    }

    @Autowired
    UserDetailsService userDetailsService;

    public UserDetails getUserDetailsFromToken(String token) {
        String username = extractUserNameFromToken(token);
        return userDetailsService.getUserByUsername(username);
    }

    private String extractUserNameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getsigningkey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    private Date extractExpiryDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getsigningkey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateToken(String token) {
        Date expDate = extractExpiryDateFromToken(token);
        return expDate.before(new Date());
    }

    public ResponseEntity<SuccessResponse> generateToken(String username) {
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(getsigningkey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .compact();
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK,"success",new TokenResponse(token));
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
}
