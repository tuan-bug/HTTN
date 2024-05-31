package com.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String tokenType = "Bearer ";
    private String role;

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public AuthResponseDto(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
