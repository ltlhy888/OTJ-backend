package com.ltech.bidding.service.dto.product;


import com.ltech.bidding.service.dto.contact.ContactMethodDto;
import com.sun.istack.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProductRequestDto implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String description;
    private String location;
    private Long price;
    private Set<ContactMethodDto> contacts = new HashSet<>();
    private Set<Long> productTypeIds = new HashSet<>();
    private String priceType;
}
