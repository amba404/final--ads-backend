package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private final static int ENCODE_STRENGTH = 5;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.*",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register",
            "/image/**"
    };

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .requestMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .requestMatchers("/ads","/ads/**", "/users/**")
                                        .authenticated())
                .cors(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(ENCODE_STRENGTH);
    }

}
