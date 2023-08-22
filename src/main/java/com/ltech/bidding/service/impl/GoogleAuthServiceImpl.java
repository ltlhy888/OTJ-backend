package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.FileInfo;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.dto.auth.AuthenticationResponseDto;
import com.ltech.bidding.service.dto.auth.JwtTokenDto;
import com.ltech.bidding.service.dto.auth.LoginRequestDto;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.model.enumeration.AuthProvider;
import com.ltech.bidding.model.enumeration.UserRole;
import com.ltech.bidding.property.OAuth2Properties;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.AuthService;
import com.ltech.bidding.service.RoleService;
import com.ltech.bidding.service.enumeration.ProcessUserType;
import com.ltech.bidding.util.JwtTokenProvider;
import com.ltech.bidding.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service("GoogleAuthService")
public class GoogleAuthServiceImpl implements AuthService {
    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final RoleService roleService;
    private final FileInfoRepository fileInfoRepository;

    @Override
    public AppUser register(SignUpRequestDto signUpRequest) {
        String uriString = UriComponentsBuilder
                .fromUriString(oAuth2Properties.getGoogle().getUserInfoUri())
                .queryParam("access_token", signUpRequest.getKey())
                .build()
                .toUriString();
        ResponseEntity<Map> response = restTemplate.getForEntity(uriString, Map.class);
        String email = (String) response.getBody().get("email");
        String name = (String) response.getBody().get("name");
        String id = (String) response.getBody().get("id");
        String picture = (String) response.getBody().get("picture");

        AppUser user = new AppUser();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(UUID.randomUUID().toString());
        user.setIp(signUpRequest.getIp());
        user.setLocked(false);
        user.setEnabled(true);
        user.setProviderId(id);
        user.setProvider(AuthProvider.GOOGLE);

        String avatarId = UUID.randomUUID().toString();

        FileInfo avatar = new FileInfo();
        avatar.setId(avatarId);
        avatar.setFileType("image/*");
        avatar.setUrl(picture);
        user.setAvatar(avatar);
        user.setLastSignedIn(new Date());
        roleService.assignUserNewRole(user, UserRole.USER.name());

        userRepository.save(user);
        return user;
    }

    @Override
    public AuthenticationResponseDto login(LoginRequestDto loginRequest) {
        String uriString = UriComponentsBuilder
                .fromUriString(oAuth2Properties.getGoogle().getUserInfoUri())
                .queryParam("access_token", loginRequest.getKey())
                .build()
                .toUriString();
        ResponseEntity<Map> response = restTemplate.getForEntity(uriString, Map.class);

        String id = (String) response.getBody().get("id");

        AppUser user = userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, id).orElseThrow();

        JwtTokenDto access_token = tokenProvider.generateAccessToken(user.getId());
        JwtTokenDto refresh_token = tokenProvider.generateRefreshToken(user.getId());

        user.setLastSignedIn(new Date());
        userRepository.save(user);

        return new AuthenticationResponseDto(access_token.getToken(), refresh_token.getToken(),"Success!", access_token.getExpiredAt().getTime());
    }

    @Override
    public Map<String, String> processToken(String email, ProcessUserType processUserType) {
        return null;
    }


    @Override
    public boolean resetPassword(String newPassword, String token) {
        return false;
    }

    @Override
    public boolean activateAccount(String token) {
        return false;
    }
}
