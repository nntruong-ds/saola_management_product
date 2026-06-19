package com.example.demo36.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // filter chain
@EnableMethodSecurity // @PreAuthorize trên method controller



public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Dùng để mã hóa mật khẩu
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Tắt CSRF để gọi API từ Postman dễ dàng
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Cho phép vào Login tự do
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/api/products/myproducts").permitAll()\
                        .requestMatchers(HttpMethod.POST, "/api/products/**").authenticated()
                        .anyRequest().authenticated() // Mọi API khác (như search sản phẩm) đều phải có Token
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không dùng Session truyền thống
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT vào trước

        return http.build();
    }
}
