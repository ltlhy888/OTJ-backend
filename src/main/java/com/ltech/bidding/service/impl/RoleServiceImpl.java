package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.AppRole;
import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.repository.RoleRepository;
import com.ltech.bidding.repository.UserRepository;
import com.ltech.bidding.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public boolean assignUserRoles(String userId,  Set<String> roleNames) {
        AppUser user = userRepository.getReferenceById(userId);
        Set<AppRole> roles = roleRepository.findByNameIn(roleNames);

        if(!roles.isEmpty()) {
            roles.forEach(r -> {
                user.getRoles().add(r);
            });

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean assignUserRoles(AppUser user, Set<String> roleNames) {
        Set<AppRole> roles = roleRepository.findByNameIn(roleNames);

        if(!roles.isEmpty()) {
            roles.forEach(r -> {
                user.getRoles().add(r);
            });

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean assignUserNewRole(String userId, String roleName) {
        AppUser user = userRepository.getReferenceById(userId);
        AppRole role  = roleRepository.findByName(roleName).orElseThrow();

        if(user.getRoles().stream().noneMatch(r -> r.getName().equals(roleName))) {
            user.getRoles().add(role);

            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean assignUserNewRole(AppUser user, String roleName) {
        AppRole role  = roleRepository.findByName(roleName).orElseThrow();

        if(user.getRoles().stream().noneMatch(r -> r.getName().equals(roleName))) {
            user.getRoles().add(role);

            userRepository.save(user);

            return true;
        }

        return false;
    }
}
