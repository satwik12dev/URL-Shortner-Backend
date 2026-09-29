package com.url.shortener.dtos;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
