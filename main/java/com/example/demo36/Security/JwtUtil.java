package com.example.demo36.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    // Lưu ý: Key phải đủ dài (tối thiểu 256-bit) cho thuật toán HS256
    private final String SECRET_KEY = "NguyenngocTruongsieudeptrainhatthegioikhongaicothebangdc";

    // Trả về thẳng SecretKey để tránh phải ép kiểu thủ công
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo Token từ Username và Role
    public String generateToken(String username, String role, Long userId) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 giờ
                .signWith(getSigningKey()) // Phiên bản mới tự nhận diện thuật toán HS256 dựa trên độ dài Key
                .compact();
    }

    // Hàm bổ trợ để đọc toàn bộ Claims (Payload) một cách thống nhất
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Lấy Username từ Token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Lấy Role từ Token (Đã sửa lỗi build/parse cũ)
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Lấy userId từ Token
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }


    // Kiểm tra token hợp lệ
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = extractAllClaims(token);
            String extractedUsername = claims.getSubject();
            Date expiration = claims.getExpiration();

            return extractedUsername.equals(username) && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}