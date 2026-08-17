package com.shopfloorquality.qualityinspection.service;

import com.shopfloorquality.qualityinspection.dto.AuthResponse;
import com.shopfloorquality.qualityinspection.dto.LoginRequest;
import com.shopfloorquality.qualityinspection.dto.RegisterRequest;
import com.shopfloorquality.qualityinspection.entity.User;
import com.shopfloorquality.qualityinspection.enums.UserRole;
import com.shopfloorquality.qualityinspection.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .userRole(UserRole.SUPERVISOR)
                .build();
        
        userService.save(user);
        
        String token = jwtUtil.generateToken(user);
        
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getUserRole().name())
                .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        User user = userService.findByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user);
        
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getUserRole().name())
                .build();
    }
}
