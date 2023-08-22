package com.ltech.bidding.service.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PriceDto {
    private String productId;
    @NotNull
    private Long price;
    private String description;
    @NotNull
    private String currency;
    @NotNull
    private String priceType;
}