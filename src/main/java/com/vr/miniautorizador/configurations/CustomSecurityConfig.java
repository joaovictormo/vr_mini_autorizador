package com.vr.miniautorizador.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomSecurityConfig {

  @Bean
  SecurityFilterChain filterChain(
      final HttpSecurity http, final CustomBasicAuthenticationEntryPoint entryPoint)
      throws Exception {

    return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(basic -> basic.authenticationEntryPoint(entryPoint))
        .build();
  }

  @Bean
  UserDetailsService userDetailsService(final PasswordEncoder passwordEncoder) {
    final UserDetails user =
        User.withUsername("username")
            .password(passwordEncoder.encode("password"))
            .authorities("ROLE_USER")
            .build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
