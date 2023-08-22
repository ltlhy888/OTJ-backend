package com.ltech.bidding.service.impl;

import com.ltech.bidding.service.dto.contact.SendEmailDto;
import com.ltech.bidding.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public Boolean send(SendEmailDto sendEmailDto) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(sendEmailDto.getTo());
            mimeMessageHelper.setSubject(sendEmailDto.getSubject());
            mimeMessageHelper.setText(sendEmailDto.getContent(),true);

            mailSender.send(message);
        } catch (Exception ex) {
            log.error(ex.toString());
            return false;
        }

        return true;
    }
}
