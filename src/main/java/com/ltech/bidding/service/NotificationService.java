package com.ltech.bidding.service;

import com.ltech.bidding.service.dto.contact.SendNotificationDto;

public interface NotificationService {
    boolean sendMsg(SendNotificationDto sendNotificationDto);
}
