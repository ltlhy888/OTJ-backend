package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.service.dto.auth.SignUpRequestDto;
import com.ltech.bidding.model.enumeration.UserRole;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.AbstractAuthService;
import com.ltech.bidding.service.RoleService;
import com.ltech.bidding.service.enumeration.ProcessUserType;
import com.ltech.bidding.util.JwtTokenProvider;
import com.ltech.bidding.util.TokenGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Transactional
@Service("AdminAuthService")
public class AdminAuthServiceImpl extends AbstractAuthService {
    private final RoleService roleService;

    public AdminAuthServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, TokenGenerator tokenGenerator, RoleService roleService) {
        super(passwordEncoder, authenticationManager, tokenProvider, userRepository);
        this.roleService = roleService;
    }


    @Override
    public AppUser register(SignUpRequestDto signUpRequest) {
        AppUser user = super.register(signUpRequest);
        roleService.assignUserRoles(user.getId(),new HashSet<>(List.of(UserRole.ADMIN.name())));

        return user;
    }

    @Override
    public Map<String, String> processToken(String email, ProcessUserType processUserType) {
        return null;
    }
}
