package com.ltech.bidding.controller;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.UserPrincipal;

import com.ltech.bidding.model.enumeration.AuthProvider;
import com.ltech.bidding.model.enumeration.UserRoleAccess;
import com.ltech.bidding.property.OAuth2Properties;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.AuthService;
import com.ltech.bidding.service.RoleService;
import com.ltech.bidding.service.dto.auth.AuthenticationResponseDto;
import com.ltech.bidding.service.dto.general.ErrorDto;
import com.ltech.bidding.service.dto.auth.LoginRequestDto;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.service.enumeration.ProcessUserType;
import com.ltech.bidding.service.impl.OAuth2AuthUrIFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final OAuth2AuthUrIFactory oAuth2AuthUrIFactory;
    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;

    private final AuthService googleAuthService;

    public AuthController(@Qualifier("UserAuthService") AuthService authService, UserRepository userRepository, RoleService roleService, OAuth2AuthUrIFactory oAuth2AuthUrIFactory, RestTemplate restTemplate, OAuth2Properties oAuth2Properties, @Qualifier("GoogleAuthService") AuthService googleAuthService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.oAuth2AuthUrIFactory = oAuth2AuthUrIFactory;
        this.restTemplate = restTemplate;
        this.oAuth2Properties = oAuth2Properties;

        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto loginRequest) {
        try {
            AuthenticationResponseDto responseBody =  authService.login(loginRequest);

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }


    @GetMapping("/thirdparty/{provider}")
    public ResponseEntity<?> getThirdPartyUrl(@PathVariable String provider) {
        String uri = oAuth2AuthUrIFactory.getUri(provider);

        return ResponseEntity.ok().body(uri);
    }

    @PostMapping("/register/google")
    public ResponseEntity<?> registerWithTGoogle(@RequestBody String accessToken, HttpServletRequest request) {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setKey(accessToken);
        signUpRequestDto.setIp(request.getRemoteAddr());

        googleAuthService.register(signUpRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody String accessToken) {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setKey(accessToken);

        return ResponseEntity.ok().body(googleAuthService.login(loginRequestDto));
    }

    @PostMapping("/thirdparty/{provider}")
    public ResponseEntity<?> isRegisteredWithThirdParty(@PathVariable String provider,@RequestBody String accessToken) {
        String uriString = UriComponentsBuilder
                .fromUriString(oAuth2Properties.getGoogle().getUserInfoUri())
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
        ResponseEntity<Map> response = restTemplate.getForEntity(uriString, Map.class);

        String id = (String) response.getBody().get("id");

        userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, id).orElseThrow();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody SignUpRequestDto signUpRequest, HttpServletRequest request) {
        try {
            signUpRequest.setIp(request.getRemoteAddr());
            authService.register(signUpRequest);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(403).body(new ErrorDto(ex.getMessage(), ex.getClass().getSimpleName()));
        }
    }

    @PostMapping("/attempt/resetpwd/{email}")
    public ResponseEntity<?> attemptToResetPwd(@PathVariable String email) {
        return ResponseEntity.ok().body(authService.processToken(email, ProcessUserType.RESET_PASSWORD));
    }
    @PostMapping("/resetpwd/{token}")
    public ResponseEntity<?> resetPwd(@PathVariable String token, @RequestBody String newPassword) {

        authService.resetPassword(newPassword, token);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/role")
    public ResponseEntity<?> selectRoles(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody String[] roles) {
        // Get all public roles
        Set<String> newRoles = Arrays.stream(roles)
                .filter(r -> r.equals(UserRoleAccess.PUBLIC.name()))
                .collect(Collectors.toSet());

        roleService.assignUserRoles(userPrincipal.getId(), newRoles);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable String token) {
        if(authService.activateAccount(token)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/attempt/activate/{email}")
    public ResponseEntity<?> attemptToActivateUser(@PathVariable String email) {
        return ResponseEntity.ok().body(authService.processToken(email, ProcessUserType.ACCOUNT_ACTIVATION));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public  ResponseEntity<?> me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        AppUser user = userPrincipal.user();

        return ResponseEntity.ok().body(user);
    }

}
