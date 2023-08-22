package com.ltech.bidding.controller;

import com.ltech.bidding.model.UserPrincipal;
import com.ltech.bidding.service.dto.PayDto;
import com.ltech.bidding.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/pay")
    public ResponseEntity<String> pay(@Validated @RequestBody PayDto payDto, @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        payDto.setCustomerId(userPrincipal.getId());

        String checkoutUrl = (String)paymentService.pay(payDto);

        return ResponseEntity.ok().body(checkoutUrl);
    }

    @PostMapping("/pay/status")
    public void paid(HttpEntity<String> httpEntity) throws Exception{
        paymentService.processResult(httpEntity.getBody(), httpEntity.getHeaders().getFirst("Stripe-Signature"));
    }
}
