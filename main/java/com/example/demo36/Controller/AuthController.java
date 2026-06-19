package com.example.demo36.Controller;

import com.example.demo36.Service.DTO.LoginRequest;
import com.example.demo36.Service.DTO.LoginResponse;
import com.example.demo36.Service.DTO.RegiterReq;
import com.example.demo36.Service.DTO.UserGet;
import com.example.demo36.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.saolasoft.base.api.response.APIResponse;
import vn.saolasoft.base.api.response.APIResponseHeader;
import vn.saolasoft.base.api.response.APIResponseStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserGet>> register(@Valid @RequestBody RegiterReq req) {
        UserGet created = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(
                new APIResponseHeader(APIResponseStatus.CREATED, "User registered"), created));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(new APIResponse<>(
                new APIResponseHeader(APIResponseStatus.OK, "Login success"),
                userService.login(req)));
    }
}
