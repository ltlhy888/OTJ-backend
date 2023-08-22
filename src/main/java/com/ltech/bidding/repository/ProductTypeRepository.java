package com.ltech.bidding.repository;

import com.ltech.bidding.model.Product;
import com.ltech.bidding.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    Set<ProductType> findByIdIn(Set<Long> ids);
    Set<ProductType> findByDisplay(Boolean display);
}
