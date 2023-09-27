package com.example.finalprojectbackend.lab2you.config.security;

import com.example.finalprojectbackend.lab2you.api.filters.JWTAuthenticationFilter;
import com.example.finalprojectbackend.lab2you.api.filters.JWTAuthorizationFilter;
import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.config.WebMvcConfigCors;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(request ->
                                    permitAllRequestMatchers().stream().anyMatch(matcher -> matcher.matches(request))
                            ).permitAll()
                            .requestMatchers(request ->
                                    adminRequestMatchers().stream().anyMatch(matcher -> matcher.matches(request)))
                            .hasAuthority(
                                    Lab2YouConstants.lab2YouRoles.ADMIN.getRole())
                            .requestMatchers(request ->
                                    medicalRequestMatchers().stream().anyMatch(matcher -> matcher.matches(request)))
                            .hasAuthority(
                                    Lab2YouConstants.lab2YouRoles.MEDICAL.getRole())
                            .anyRequest().authenticated();
                })
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, failed) -> {
                        var responseWrapper = new ResponseWrapper();
                        responseWrapper.setSuccessful(false);
                        responseWrapper.setMessage("Authentication failed");
                        responseWrapper.setData(Collections.emptyList());

                        response.getWriter().write(
                                new ObjectMapper().writeValueAsString(
                                        responseWrapper));
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().flush();
                    });
                    ex.accessDeniedHandler((request, response, failed) -> {
                        var responseWrapper = new ResponseWrapper();
                        responseWrapper.setSuccessful(false);
                        responseWrapper.setMessage("Access denied");
                        responseWrapper.setData(Collections.emptyList());

                        response.getWriter().write(
                                new ObjectMapper().writeValueAsString(
                                        responseWrapper));
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().flush();
                    });
                })
                .cors(cors -> cors.configurationSource(WebMvcConfigCors.corsConfigurationSource()))
                .build();
    }

    /*
     * This method is used to load the user by username.
     * It is used by the authentication manager to validate the user if exists in
     * the record.
     *
     */

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
            throws Exception {
        var authenticationManagerBuilder = new DaoAuthenticationProvider();
        authenticationManagerBuilder.setUserDetailsService(userDetailsService);
        authenticationManagerBuilder.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationManagerBuilder);

    }

    /*
     * This method is used to encode the password.
     */

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CorsFilter(WebMvcConfigCors.corsConfigurationSource()));
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterFilterRegistrationBean;
    }


    private List<RequestMatcher> permitAllRequestMatchers() {
        return List.of(
                new AntPathRequestMatcher("/api/v1/request/**", "GET"),
                new AntPathRequestMatcher("/api/v1/request/**", "POST"),
                new AntPathRequestMatcher("/api/v1/customer/register", "POST"),
                new AntPathRequestMatcher("/api/v1/catalog/**", "GET"),
                new AntPathRequestMatcher("/api/v1/catalog/**", "POST"),
                new AntPathRequestMatcher("/api/v1/catalog/**", "PUT"),
                new AntPathRequestMatcher("/api/v1/catalog/**", "DELETE"),
                new AntPathRequestMatcher("/doc/**", "GET"),
                new AntPathRequestMatcher("/v3/api-docs/**", "GET"),
                new AntPathRequestMatcher("/api/v1/login", "POST"));
    }

    private List<RequestMatcher> adminRequestMatchers() {
        return List.of(new AntPathRequestMatcher("/api/v1/userList", "GET"),
                new AntPathRequestMatcher("/api/v1/registerUserFromInternalRequest", "POST"));
    }

    private List<RequestMatcher> medicalRequestMatchers() {
        return List.of(new AntPathRequestMatcher("/api/v1/userList", "GET"),
                new AntPathRequestMatcher("/api/v1/registerUserFromMedicalRequest", "POST"));
    }


}
