package com.ltech.bidding.service;

import com.ltech.bidding.model.AppUser;

import java.util.Set;

public interface RoleService {
    boolean assignUserRoles(String userId, Set<String> roleNames);
    boolean assignUserRoles(AppUser user, Set<String> roleNames);

    boolean assignUserNewRole(String userId, String roleName);

    boolean assignUserNewRole(AppUser user, String roleName);
}
