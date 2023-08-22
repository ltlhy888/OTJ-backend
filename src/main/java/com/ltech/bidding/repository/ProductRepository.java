package com.ltech.bidding.repository;

import com.ltech.bidding.model.AppRole;
import com.ltech.bidding.model.Product;
import com.ltech.bidding.model.enumeration.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("select p from Product p where p.id = ?1 and p.user.id = ?2 ")
    Optional<Product> findByIdAndUserId(String id, String userId);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}
