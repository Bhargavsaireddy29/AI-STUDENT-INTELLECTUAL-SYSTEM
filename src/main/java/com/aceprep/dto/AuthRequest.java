package com.aceprep.dto;

public record AuthRequest(
        String email,
        String password
) {}