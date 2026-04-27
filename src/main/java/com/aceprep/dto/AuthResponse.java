package com.aceprep.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}