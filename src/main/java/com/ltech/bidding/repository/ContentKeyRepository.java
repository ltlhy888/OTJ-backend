package com.ltech.bidding.repository;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.ContentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentKeyRepository extends JpaRepository<ContentKey,String> {
}
