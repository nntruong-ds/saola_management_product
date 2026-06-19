package com.example.demo36.Service.Impl;
import com.example.demo36.Entity.User;
import com.example.demo36.Repository.UserRepository;
import com.example.demo36.Security.JwtUtil;
import com.example.demo36.Service.DTO.LoginRequest;
import com.example.demo36.Service.DTO.LoginResponse;
import com.example.demo36.Service.DTO.RegiterReq;
import com.example.demo36.Service.DTO.UserGet;
import com.example.demo36.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.saolasoft.base.exception.APIAuthenticationException;
import vn.saolasoft.base.exception.DuplicateIdentifierException;
import vn.saolasoft.base.persistence.repository.VoidableRepository;
import vn.saolasoft.base.service.impl.VoidableDtoJpaServiceImpl;
import vn.saolasoft.base.util.AuditUtil;

import java.util.Set;

@Service
public class UserServiceImpl extends VoidableDtoJpaServiceImpl<UserGet, User, Long> implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public VoidableRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public UserGet convert(User user) {
        return new UserGet(user);
    }

    @Override
    public Set<String> getSortableColumns() {
        return Set.of("id", "username", "dateCreated");
    }

    @Override
    public UserGet register(RegiterReq req) {
        if (userRepository.existsByUsernameAndVoidedFalse(req.getUsername())) {
            throw new DuplicateIdentifierException("Username ton tai " + req.getUsername());
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setVoided(Boolean.FALSE);
        AuditUtil.addCreationInformation(user, null);
        return convert(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        User user = authenticate(req.getUsername(), req.getPassword());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
        return new LoginResponse(token);
    }

    //@Override
    public User authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsernameAndVoidedFalse(username)
                .orElseThrow(() -> new APIAuthenticationException("Sai tên đăng nhập hoặc mật khẩu!"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new APIAuthenticationException("Sai tên đăng nhập hoặc mật khẩu!");
        }
        return user;
    }
}