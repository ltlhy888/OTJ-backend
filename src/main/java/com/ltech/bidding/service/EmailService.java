package com.ltech.bidding.service;

import com.ltech.bidding.service.dto.contact.SendEmailDto;

public interface EmailService {
    Boolean send(SendEmailDto sendEmailDto);

}
