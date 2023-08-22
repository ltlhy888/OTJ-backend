package com.ltech.bidding.service.impl;

import com.ltech.bidding.service.dto.contact.SendNotificationDto;
import com.ltech.bidding.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class PhoneNotificationServiceImpl implements NotificationService {
    @Override
    public boolean sendMsg(SendNotificationDto sendNotificationDto) {
        return false;
    }
}
