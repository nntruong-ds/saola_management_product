package com.example.demo36.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy chuỗi "Authorization" từ Header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. Kiểm tra xem có bắt đầu bằng "Bearer " không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Tránh việc token lỗi/hết hạn làm sập filter, chỉ cần không xử lý tiếp
                logger.error("Không thể giải mã Token: " + e.getMessage());
            }

            // 3. Nếu lấy được username và SecurityContext chưa có authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 4. Kiểm tra token có hợp lệ không
                if (jwtUtil.validateToken(token, username)) {

                    // --- ĐOẠN SỬA ĐỔI ĐỂ THÊM ROLE ---
                    // Lấy role từ token (Ví dụ: "ADMIN" hoặc "USER")
                    String role = jwtUtil.extractRole(token);
                    Long userId = jwtUtil.extractUserId(token);
                    // Chuyển đổi thành quyền GrantedAuthority hợp lệ của Spring Security
                    // Lưu ý: Thêm "ROLE_" phía trước nếu chuỗi trong token chưa có.
                    String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));

                    // 5. Tạo authentication token chứa danh sách quyền (authorities) vừa lấy được
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);

                    // 6. Set vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Cho phép request tiếp tục đi tới Controller hoặc các Filter tiếp theo
        filterChain.doFilter(request, response);
    }
}