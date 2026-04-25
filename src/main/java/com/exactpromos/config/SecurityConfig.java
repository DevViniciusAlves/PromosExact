package com.exactpromos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${security.admin.username:admin}")
    private String adminUsername;

    @Value("${security.admin.password-hash:}")
    private String adminPasswordHash;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/produtos/**", "/promocoes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated()
                        .requestMatchers("/telegram/**", "/usuarios/**", "/interesses/**", "/watchlist/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/produtos/**", "/promocoes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/produtos/link-afiliado/testar").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/**", "/produtos/**", "/promocoes/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**", "/produtos/**", "/promocoes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**", "/produtos/**", "/promocoes/**").authenticated()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        if (adminPasswordHash == null || adminPasswordHash.isBlank()) {
            throw new IllegalStateException("security.admin.password-hash nao configurado");
        }

        return new InMemoryUserDetailsManager(
                User.withUsername(adminUsername)
                        .password(adminPasswordHash)
                        .roles("ADMIN")
                        .build()
        );
    }
}
