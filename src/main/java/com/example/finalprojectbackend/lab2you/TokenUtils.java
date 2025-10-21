package com.example.finalprojectbackend.lab2you;

import com.example.finalprojectbackend.lab2you.config.security.UserDetailsImpl;
import com.example.finalprojectbackend.lab2you.db.model.entities.ModuleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "0fhqd2mnCqlSTGSOTfZhbg7rXfaStyPc";
    private final static Long EXPIRATION_TIME = 86_400L;// 24 hours in seconds

    /*
     * This method will create a token with the email and name of the user.
     * The token will expire in 30 days.
     * The token will be signed with the ACCESS_TOKEN_SECRET.
     * The token will have the email as subject.
     * The token will have the name as extra information.
     * The token will have the expiration date.
     */
    public static String createToken(UserDetailsImpl userDetails) {
        long expirationTime = EXPIRATION_TIME * 1000; // Convert to milliseconds
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("id", userDetails.getUserId());
        extra.put("name", userDetails.getName());
        extra.put("userType", userDetails.getUserType());
        extra.put("Nit", userDetails.getNit());
        extra.put("authorities", userDetails.getAuthorities().stream()
                .map(authority -> Map.of("authority", authority.getAuthority()))
                .collect(Collectors.toList()));
        extra.put("role", userDetails.getRole());
        extra.put("modules", userDetails.getModules());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    /*
     * This method will return the Authentication object with the username, password
     * and authorities.
     * if the token is not valid will return null.
     */

    @SuppressWarnings("unchecked")
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            List<GrantedAuthority> authorities = ((List<Map<String, String>>) claims.get("authorities"))
                    .stream()
                    .map(authorityMap -> new SimpleGrantedAuthority(authorityMap.get("authority")))
                    .collect(Collectors.toList());


            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (JwtException e) {
            return null;
        }

    }
}
