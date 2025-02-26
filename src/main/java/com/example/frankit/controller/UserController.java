package com.example.frankit.controller;

import com.example.frankit.dto.request.user.LoginRequestDto;
import com.example.frankit.dto.request.user.SignUpRequestDto;
import com.example.frankit.dto.response.ApiSuccessResponse;
import com.example.frankit.jwt.JwtUtil;
import com.example.frankit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    // 1. 회원 가입 API
    @PostMapping("/signup")
    public ApiSuccessResponse signup(@Valid @RequestBody SignUpRequestDto dto) {
        userService.createUser(dto.toDto());
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 2. 로그인 API
    @PostMapping("/login")
    public ApiSuccessResponse login(@Valid @RequestBody LoginRequestDto dto) throws AuthenticationException {
        // 사용자 인증
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword()));

        // 토큰 생성, userId 기반
        String token = jwtUtil.generateToken(dto.getUserId());

        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", token);
    }
}
