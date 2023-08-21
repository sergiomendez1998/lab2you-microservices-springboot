package com.example.finalprojectbackend.lab2you.config.security;


import com.example.finalprojectbackend.lab2you.api.filters.JWTAuthenticationFilter;
import com.example.finalprojectbackend.lab2you.api.filters.JWTAuthorizationFilter;
import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.config.WebMvcConfigCors;
import lombok.AllArgsConstructor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    /*
    * This method is used to configure the security filter chain.
    * It is used to configure the security filter chain.
    *
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/api/v1/registerUserFromExternalRequest","POST")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/api/v1/userList","GET"),
                        new AntPathRequestMatcher("/api/v1/registerUserFromInternalRequest","POST")
                ).hasAuthority(
                        Lab2YouConstants.lab2YouRoles.ADMIN.getRole()
                )
                .requestMatchers(
                        new AntPathRequestMatcher("/api/v1/userList","GET"),
                        new AntPathRequestMatcher("/api/v1/registerUserFromMedicalRequest","POST")
                ).hasAuthority(
                        Lab2YouConstants.lab2YouRoles.MEDICAL.getRole()
                )
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors-> cors.configurationSource(WebMvcConfigCors.corsConfigurationSource()))
                .build();
    }

    /*
    * This method is used to load the user by username.
    * It is used by the authentication manager to validate the user if exists in the record.
    *
     */
    @SuppressWarnings("removal")
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    /*
    * This method is used to encode the password.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
        FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CorsFilter(WebMvcConfigCors.corsConfigurationSource()));
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterFilterRegistrationBean;
    }
}
