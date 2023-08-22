package com.ltech.bidding.service.dto.product;


import com.ltech.bidding.model.enumeration.ProductStatus;
import com.sun.istack.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateProductDto{
    private String productId;
    private String name;
    private String description;
    private String status;
    public ProductStatus getStatus() {
        return ProductStatus.valueOf(this.status);
    }
}
