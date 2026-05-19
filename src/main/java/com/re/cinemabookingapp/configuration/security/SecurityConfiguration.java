package com.re.cinemabookingapp.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AjaxAwareAuthenticationSuccessHandler successHandler;
    private final AjaxAwareAuthenticationFailureHandler failureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Tài nguyên công khai
                .requestMatchers(
                    "/", "/api/auth/register", "/process-login",
                    "/css/**", "/js/**", "/assets/**", "/error"
                ).permitAll()

                // Trang login riêng
                .requestMatchers("/admin/login", "/staff/login").permitAll()

                // Phân quyền theo role
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/pos/**").hasAnyRole("ADMIN", "STAFF")

                // Tất cả request khác cần xác thực
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/")          // Trang login mặc định khi redirect
                .loginProcessingUrl("/process-login") // URL Spring Security xử lý xác thực
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?loggedOut=true")
                .permitAll()
            );

        return http.build();
    }
}
