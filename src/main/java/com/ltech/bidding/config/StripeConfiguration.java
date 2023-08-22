package com.ltech.bidding.config;

import com.stripe.Stripe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StripeConfiguration {

    @Value("${stripe.api-key}")
    private String apiKey;
    @EventListener(ApplicationReadyEvent.class)
    public void setUpStripe() {
        Stripe.apiKey = this.apiKey;
        log.info("Finished setup stripe api");

    }
}