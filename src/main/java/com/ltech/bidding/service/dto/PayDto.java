package com.ltech.bidding.service.dto;


import com.sun.istack.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class PayDto implements Serializable {
    @NotNull
    private Long priceId;
    private String customerId;
    private String successUrl;
    private String cancelUrl;
    @NotNull
    private Long quantity;
}
