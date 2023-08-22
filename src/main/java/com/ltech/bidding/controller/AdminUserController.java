package com.ltech.bidding.controller;

import com.ltech.bidding.service.dto.AssignRolesDto;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @PostMapping("/role/assign")
    public ResponseEntity<?> assignRolesToUser(@RequestBody AssignRolesDto assignRolesDto) {
        boolean isSuccess = roleService.assignUserRoles(assignRolesDto.getUserId(), assignRolesDto.getRoleNames());

        if(isSuccess) return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().body("Role doesn't exist");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }
}
