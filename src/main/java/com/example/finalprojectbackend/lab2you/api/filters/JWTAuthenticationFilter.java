package com.example.finalprojectbackend.lab2you.api.filters;

import com.example.finalprojectbackend.lab2you.config.security.UserDetailsImpl;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.AuthWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapperRequest;
import com.example.finalprojectbackend.lab2you.TokenUtils;
import com.example.finalprojectbackend.lab2you.config.security.AuthCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /*
     * This method is called when user tries to login.
     * Will evaluate if the username and password are correct. taking the email as
     * username.
     * if this is correct will return an Authentication object with the username,
     * password and authorities.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        AuthCredentials authCredentials = null;
        try {
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(), authCredentials.getPassword(), Collections.emptyList());
        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    /*
     * This method is called when authentication is successful.
     * this method will create a JWT token and add it to the response header.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

            // var list = userDetails.getModules();

            String token = TokenUtils.createToken(userDetails.getName(), userDetails.getUsername(),
                    userDetails.getAuthorities(), userDetails.getRole(), userDetails.getModules());

            var authResponse = new AuthWrapper(token,
                    userDetails.getRole(), userDetails.getName(), userDetails.getUsername());

            var ResponseWrapper = new ResponseWrapperRequest<AuthWrapper>(authResponse, "User authenticated", true);

            response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseWrapper));
            response.setContentType("application/json");
            response.getWriter().flush();
            super.successfulAuthentication(request, response, chain, authResult);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
