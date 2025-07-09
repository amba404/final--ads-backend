package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация параметров безопасности
 */

@Configuration
public class WebSecurityConfig {

    @Value(value = "${encode.strength:5}")
    private int encodeStrength;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.*",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register",
            "/image/**",
            "/ads"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .requestMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .requestMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(encodeStrength);
    }

}
