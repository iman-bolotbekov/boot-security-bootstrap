package ru.kata.spring.boot_security.demo.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailsService;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final SuccessUserHandler successUserHandler;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          SuccessUserHandler successUserHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.successUserHandler = successUserHandler;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/login", "/auth/register", "/error").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                .formLogin(formLogin ->
                        formLogin.loginPage("/auth/login")
                                .loginProcessingUrl("/process_login")
                                .successHandler(successUserHandler)
                                .failureUrl("/auth/login?error")
                )
                .logout((logout) ->
                        logout.logoutUrl("/logout").logoutSuccessUrl("/auth/login"))
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
