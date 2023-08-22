package com.ltech.bidding.service.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class AuthenticationResponseDto {
    private String access_token;
    private String refresh_token;
    private String message;
    private Long expiredAt;
}
