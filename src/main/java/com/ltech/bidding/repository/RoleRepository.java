package com.ltech.bidding.repository;

import com.ltech.bidding.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByName(String name);
    @Query("select r.name from AppRole r join AppUser u on u.id = ?1")
    Set<String> findByUserId(String userId);
    Set<AppRole> findByNameIn(Set<String> names);

}
