package ron.com.projectmanager;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ✅ public endpoints
                        .requestMatchers("/api/auth/**", "/actuator/**").permitAll()

                        // 🔴 ADMIN only (PUT FIRST!)
                        .requestMatchers(HttpMethod.POST, "/api/tasks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasRole("ADMIN")

                        // 🟢 USER + ADMIN (read only)
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**")
                        .hasAnyRole("USER", "ADMIN")

                        // fallback
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtFilter(),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }


}