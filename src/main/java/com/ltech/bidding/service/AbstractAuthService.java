package com.ltech.bidding.service;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.FileInfo;
import com.ltech.bidding.model.PasswordHistory;
import com.ltech.bidding.model.UserPrincipal;

import com.ltech.bidding.model.enumeration.AuthProvider;
import com.ltech.bidding.model.enumeration.EmailTemplate;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.dto.auth.AuthenticationResponseDto;
import com.ltech.bidding.service.dto.auth.JwtTokenDto;
import com.ltech.bidding.service.dto.auth.LoginRequestDto;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.util.JwtTokenProvider;
import com.ltech.bidding.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAuthService implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;



    public AppUser register(SignUpRequestDto signUpRequest) {
        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) throw new IllegalArgumentException("Email exits");

        AppUser user = new AppUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setIp(signUpRequest.getIp());
        user.setLocked(false);
        user.setEnabled(false);
        user.setProvider(AuthProvider.LOCAL);
        user.setLastSignedIn(new Date());

        log.info("Register user: " + user.toString());

        return userRepository.save(user);
    }

    public AuthenticationResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        JwtTokenDto access_token = tokenProvider.generateAccessToken(userPrincipal.getId());
        JwtTokenDto refresh_token = tokenProvider.generateRefreshToken(userPrincipal.getId());

        AppUser user = userPrincipal.user();
        user.setLastSignedIn(new Date());
        userRepository.save(user);

        return new AuthenticationResponseDto(access_token.getToken(), refresh_token.getToken(),"Success!", access_token.getExpiredAt().getTime());
    }



    public boolean resetPassword(String newPassword, String token) {
        AppUser user = userRepository.findByPwdResetToken(token).orElseThrow();

        // Check is reset token expired
        if(user.getPwdResetTokenExpiredAt().compareTo(new Date()) < 0) throw new IllegalArgumentException("Reset token expired!");

        user.setPwdResetToken(null);
        user.setPwdResetTokenExpiredAt(null);
        PasswordHistory passwordHistory = new PasswordHistory();
        passwordHistory.setPassword(user.getPassword());
        user.getPasswordHistories().add(passwordHistory);
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean activateAccount(String token) {
        try {
            AppUser user = userRepository.findByActivateToken(token).orElseThrow();
            // Check is reset token expired
            if(user.getActivateTokenExpiredAt().compareTo(new Date()) < 0) throw new IllegalArgumentException("Activate token expired!");

            user.setActivateToken(null);
            user.setActivateTokenExpiredAt(null);
            user.setEnabled(true);

            userRepository.save(user);

            return true;
        } catch (Exception ex) {
            log.error(ex.toString());
            return false;
        }

    }
}
