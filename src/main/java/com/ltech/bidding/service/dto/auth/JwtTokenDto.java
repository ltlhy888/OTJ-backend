package com.ltech.bidding.service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtTokenDto {
    private String token;
    private Date expiredAt;
}
