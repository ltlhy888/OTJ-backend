package com.ltech.bidding.service.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ltech.bidding.model.*;
import com.ltech.bidding.model.enumeration.ProductStatus;
import com.ltech.bidding.service.dto.auth.UserInfoShortResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductResponseDto {
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
    private String additionalDataKey;
    private Long commentSetId;
    private Date created;
}
