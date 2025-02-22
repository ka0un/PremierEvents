package com.hapangama.premierevents.config;


import com.hapangama.premierevents.filter.JwtAuthFilter;
import com.hapangama.premierevents.repository.UserInfoRepository;
import com.hapangama.premierevents.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserInfoRepository repository;

    @Bean
    @Primary
    public UserDetailsService userDetailsService(@Lazy PasswordEncoder encoder) {
        return new UserInfoService(repository, encoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //swagger // -- Swagger UI v2
        //            "/v2/api-docs",
        //            "/swagger-resources",
        //            "/swagger-resources/**",
        //            "/configuration/ui",
        //            "/configuration/security",
        //            "/swagger-ui.html",
        //            "/webjars/**",
        //            // -- Swagger UI v3 (OpenAPI)
        //            "/v3/api-docs/**",
        //            "/swagger-ui/**"



        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/users/register").permitAll()
                        .requestMatchers("api/auth/login").permitAll()
                        .requestMatchers("/api/events/search").permitAll()
                        .requestMatchers("api/events/search/**").permitAll()
                        //swagger
                        .requestMatchers("v2/api-docs").permitAll()
                        .requestMatchers("swagger-resources").permitAll()
                        .requestMatchers("swagger-resources/**").permitAll()
                        .requestMatchers("configuration/ui").permitAll()
                        .requestMatchers("configuration/security").permitAll()
                        .requestMatchers("swagger-ui.html").permitAll()
                        .requestMatchers("webjars/**").permitAll()
                        .requestMatchers("v3/api-docs/**").permitAll()
                        .requestMatchers("swagger-ui/**").permitAll()
                        .requestMatchers("swagger-config").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.Authentication authentication)
                    throws IOException {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Logged out successfully");
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
