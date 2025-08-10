package com.hackathon.healsync.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080", // your frontend dev URL
            "http://your-frontend-domain.com", // your deployed frontend URL
            "https://chipper-phoenix-dbce4e.netlify.app", // Netlify signup page
            "https://neon-alpaca-73a373.netlify.app",// Additional Netlify frontend
            "https://688b04aa01e5d573f2a9e94c--elaborate-bublanina-4e7ee2.netlify.app",
            "https://healsync-hs.netlify.app",
            "http://127.0.0.1:3002",
            "http://127.0.0.1:5500",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:5501"
            // Localhost for testing
        ));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
