package com.library.managementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/redoc.html",
                                "/openapi.yaml",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        // actuator
                        .requestMatchers("/actuator/**").permitAll()

                        // admin-only
                        .requestMatchers("/api/books/**").hasRole("ADMIN")
                        .requestMatchers("/api/members/**").hasRole("ADMIN")

                        // member
                        .requestMatchers("/api/loans/**").hasRole("MEMBER")

                        // everything else
                        .anyRequest().authenticated()

                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
