package com.ridehailing.uber_system.config;

import com.ridehailing.uber_system.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // 🔓 PUBLIC
                        .requestMatchers(
                                "/auth/**",
                                "/driver/signup",
                                "/driver/login",
                                "/test/**",
                                "/ws/**",
                                "/topic/**",
                                "/app/**"
                        ).permitAll()

                        // 🌍 PUBLIC
                        .requestMatchers("/driver/location/nearby").permitAll()

                        // 👤 CUSTOMER (MOST SPECIFIC FIRST)
                        .requestMatchers("/driver/location/ride/**").hasRole("CUSTOMER")
                        .requestMatchers("/ride/view/**").hasRole("CUSTOMER")

                        // 🚗 DRIVER (SPECIFIC)
                        .requestMatchers("/ride/driver/**").hasRole("DRIVER")
                        .requestMatchers("/driver/location/update").hasRole("DRIVER")

                        // 👤 CUSTOMER (GENERIC — AFTER driver-specific)
                        .requestMatchers("/ride/**").hasRole("CUSTOMER")

                        // 🚗 DRIVER (GENERIC — LAST)
                        .requestMatchers("/driver/**").hasRole("DRIVER")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
