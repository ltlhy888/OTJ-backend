package com.ltech.bidding.service;

import com.ltech.bidding.service.dto.PayDto;

public interface PaymentService {
    Object pay(PayDto payDto) throws Exception;
    Object subscribe(PayDto payDto) throws Exception;
    void paidSuccess(String id);
    void paidFailed(String id);
    void processResult(Object payload, String secret) throws Exception;
}
