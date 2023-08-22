package com.ltech.bidding.service.dto.contact;

import lombok.Data;

@Data
public class SendNotificationDto {
    private String to;
    private String msg;
}
