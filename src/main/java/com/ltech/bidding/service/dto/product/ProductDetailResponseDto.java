package com.ltech.bidding.service.dto.product;

import com.ltech.bidding.model.ContactMethodSet;
import com.ltech.bidding.model.Price;
import com.ltech.bidding.model.ProductType;
import com.ltech.bidding.model.enumeration.ProductStatus;
import com.ltech.bidding.service.dto.auth.UserInfoShortResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@NoArgsConstructor
public class ProductDetailResponseDto{
    private String id;
    private String name;
    private String description;
    private ProductStatus status;
    private String location;
    private ContactMethodSet contactMethodSet;
    private Price currentPrice;
    private Set<String> images;
    private UserInfoShortResponseDto user;
    private Set<ProductType> productTypes = new HashSet<>();
    private Long commentSetId;
    private Date created;
    private Collection<Map<String, Object>> additionalData;
}
