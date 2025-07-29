package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private static final String SUPER_ADMIN_USERNAME = "superadmin";
    private static final String SUPER_ADMIN_PASSWORD = "superadmin123";

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authenticationProvider(new DaoAuthenticationProvider() {
                    {
                        setUserDetailsService(superAdminDetailsService());
                        setPasswordEncoder(passwordEncoder());
                    }
                })
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/register", "/login", "/css/**").permitAll()
                // USER: chỉ xem danh sách
                .requestMatchers("/users").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
                // ADMIN: CRUD users
                .requestMatchers("/users/create", "/users/{id}/edit", "/users/{id}/update").hasRole("ADMIN")
                .requestMatchers("/users/{id}").hasRole("ADMIN") // DELETE
                .requestMatchers("/users").hasRole("ADMIN") // POST (create)
                // SUPER_ADMIN: quản lý accounts
                .requestMatchers("/admin/accounts/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/admin/dashboard").hasRole("SUPER_ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/users", true)
                .permitAll()
                )
                .logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .permitAll());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager superAdminDetailsService() {
        UserDetails superAdmin = User.builder()
                .username(SUPER_ADMIN_USERNAME)
                .password(passwordEncoder().encode(SUPER_ADMIN_PASSWORD))
                .roles("SUPER_ADMIN")
                .build();
        return new InMemoryUserDetailsManager(superAdmin);
    }

    // Remove this bean
    /*
    @Bean
    public AuthenticationManagerBuilder authManagerBuilder(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider())
                .userDetailsService(superAdminDetailsService());
        return auth;
    }
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
