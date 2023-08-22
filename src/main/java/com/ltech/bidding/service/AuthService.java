package com.ltech.bidding.service;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.service.dto.auth.AuthenticationResponseDto;
import com.ltech.bidding.service.dto.auth.LoginRequestDto;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.service.enumeration.ProcessUserType;

import java.util.Map;

public interface AuthService {
    AppUser register(SignUpRequestDto signUpRequest);
    AuthenticationResponseDto login(LoginRequestDto loginRequest);
    Map<String,String> processToken(String email, ProcessUserType processUserType);
    boolean resetPassword(String newPassword, String token);
    boolean activateAccount(String token);
}
