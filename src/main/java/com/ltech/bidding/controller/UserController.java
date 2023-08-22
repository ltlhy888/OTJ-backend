package com.ltech.bidding.controller;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.UserPrincipal;
import com.ltech.bidding.model.enumeration.UserRole;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.dto.user.ProfileRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final FileInfoRepository fileInfoRepository;

    public UserController(FileInfoRepository fileInfoRepository,
                          UserRepository userRepository) {
        this.fileInfoRepository = fileInfoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/contractors")
    public ResponseEntity<?> getContractors(Pageable pageable) {
        Set<String> roleNames = new HashSet<>();
        roleNames.add(UserRole.CONTRACTOR.name());
        return ResponseEntity.ok().body(userRepository.findByRoleNameIn(roleNames,pageable));

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Validated @RequestBody ProfileRequestDto profileRequestDto,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        AppUser user = userPrincipal.user();

        if(profileRequestDto.getAvatarId() != null) {
            user.setAvatar(fileInfoRepository.findById(profileRequestDto.getAvatarId()).orElse(null));
        }

        user.setUsername(profileRequestDto.getUsername());

        return ResponseEntity.ok().body(userRepository.save(user));
    }

}
