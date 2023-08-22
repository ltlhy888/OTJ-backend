package com.ltech.bidding.repository;

import com.ltech.bidding.model.ContactMethod;
import com.ltech.bidding.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMethodRepository extends JpaRepository<ContactMethod, Long> {
}
