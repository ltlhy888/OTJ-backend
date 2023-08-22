package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.enumeration.ContactMethodType;
import com.ltech.bidding.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationServiceFactory {
    private final EmailNotificationServiceImpl emailNotificationService;
    private final PhoneNotificationServiceImpl phoneNotificationService;
    private final BaseNotificationServiceImpl normalNotificationService;

    public NotificationService createNotificationService(ContactMethodType contactMethodType) {
        switch (contactMethodType) {
            case EMAIL:
                return emailNotificationService;
            case PHONE:
                return phoneNotificationService;
            default:
                return normalNotificationService;
        }
    }
}
