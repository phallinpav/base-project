package com.sample.base_project.base.security;

import com.sample.base_project.base.security.handler.CustomAccessDeniedHandler;
import com.sample.base_project.base.security.handler.CustomEntryPoint;
import com.sample.base_project.base.security.filter.TokenAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
public abstract class SecurityConfig {

    @Autowired
    private TokenAuthFilter tokenAuthFilter;
    @Autowired
    private CustomEntryPoint entryPoint;
    @Autowired
    private CustomAccessDeniedHandler denyHandler;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setMaxAge(Duration.ofMinutes(10));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    public abstract void setAuthorizeEndpoint(HttpSecurity http) throws Exception;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainApi(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                );

        setAuthorizeEndpoint(http);

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(denyHandler)
                )
                .addFilter(tokenAuthFilter); // adjust filter position as needed

        return http.build();
    }

}
