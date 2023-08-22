package com.ltech.bidding.repository;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.enumeration.AuthProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<AppUser,String> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPwdResetToken(String token);

    Optional<AppUser> findByActivateToken(String token);
    Optional<AppUser> findByProviderAndProviderId(AuthProvider provider, String providerId);

    @Query(nativeQuery = true, value = "SELECT * FROM user u " +
            "INNER JOIN user_roles ur ON ur.user_id = u.id " +
            "INNER JOIN role r ON ur.role_id = r.id " +
            "WHERE r.name IN (:names)")
    Page<AppUser> findByRoleNameIn(Set<String> names, Pageable pageable);
}
