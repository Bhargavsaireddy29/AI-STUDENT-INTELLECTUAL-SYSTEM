package com.aceprep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aceprep.dto.AuthRequest;
import com.aceprep.dto.AuthResponse;
import com.aceprep.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {

        // TODO: validate user from DB

        String access = "mock-access-token";
        String refresh = "mock-refresh-token";

        return ResponseEntity.ok(new AuthResponse(access, refresh));
    }
}