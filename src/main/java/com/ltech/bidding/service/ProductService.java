package com.ltech.bidding.service;

import com.ltech.bidding.model.AppUser;
import com.ltech.bidding.model.Price;
import com.ltech.bidding.model.Product;
import com.ltech.bidding.service.dto.PriceDto;
import com.ltech.bidding.service.dto.product.ProductDetailResponseDto;
import com.ltech.bidding.service.dto.product.ProductRequestDto;
import com.ltech.bidding.service.dto.product.ProductResponseDto;
import com.ltech.bidding.service.dto.product.UpdateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ProductService {


    Page<ProductResponseDto> get(Pageable pageable);
    ProductResponseDto get(String id);
    Product createProduct(ProductRequestDto productRequestDto, AppUser user);
    Product updateProduct(UpdateProductDto productDto, AppUser user);
    Price createPrice(PriceDto priceDto, AppUser user);

}
