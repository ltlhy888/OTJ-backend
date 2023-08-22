package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.Price;
import com.ltech.bidding.model.Product;
import com.ltech.bidding.model.Transaction;
import com.ltech.bidding.service.dto.PayDto;
import com.ltech.bidding.model.enumeration.PriceStatus;
import com.ltech.bidding.model.enumeration.ProductStatus;
import com.ltech.bidding.model.enumeration.TransactionStatus;
import com.ltech.bidding.repository.PriceRepository;
import com.ltech.bidding.repository.ProductRepository;
import com.ltech.bidding.repository.TransactionRepository;
import com.ltech.bidding.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@Transactional
@Qualifier("stripePaymentService")
public class StripePaymentServiceImpl implements PaymentService {
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private static final String provider = "Stripe";
    private static final Logger logger = LogManager.getLogger(StripePaymentServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    public StripePaymentServiceImpl(TransactionRepository transactionRepository, ProductRepository productRepository, PriceRepository priceRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    public String pay(PayDto payDto) throws Exception{
        String transactionId = UUID.randomUUID().toString();

        // Update url for callback
        payDto.setSuccessUrl(assignTransactionIdToUrl(payDto.getSuccessUrl(), transactionId));
        payDto.setCancelUrl(assignTransactionIdToUrl(payDto.getCancelUrl(),transactionId));

        Session checkoutSession = createCheckoutSession(payDto, transactionId);

        saveTransactionFromSessionAndProduct(checkoutSession,payDto,transactionId);


        return checkoutSession.getUrl();
    }

    // TODO: subscribe to a payment
    @Override
    public Object subscribe(PayDto payDto) throws Exception {
        return null;
    }

    private void saveTransactionFromSessionAndProduct(Session checkoutSession, PayDto payDto, String id) throws Exception {
        Price price = priceRepository.getReferenceById(payDto.getPriceId());

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setPrice(price);
        transaction.setUserKey(payDto.getCustomerId());
        transaction.setCurrency(price.getCurrency());
        transaction.setThirdPartyId(checkoutSession.getId());
        transaction.setThirdPartyType(provider);
        transaction.setStatus(TransactionStatus.PENDING.name());
        transactionRepository.save(transaction);
    }
    private String assignTransactionIdToUrl(String url, String id) throws URISyntaxException {
        if(url != null) {
            URI uri = new URI(url);

            return UriComponentsBuilder.fromUri(uri).queryParam("transactionId", id).build().toString();
        }

        return "http://localhost:80";
    }

    private Session createCheckoutSession(PayDto payDto, String transactionId) throws Exception {
        Price price = priceRepository.getReferenceById(payDto.getPriceId());

        // Check price status
        if(!price.getStatus().equals(PriceStatus.OPEN.name())) {
            throw new Exception("Price is not available");
        }

        Product product = price.getProduct();

        // Check product status
        if(!product.getStatus().equals(ProductStatus.AVAILABLE.name())) {
            throw new Exception("Product is reserved");
        }

        // Guess customer expectation price
        price.setPriority(price.getPriority() + 1);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(payDto.getSuccessUrl())
                .setCancelUrl(payDto.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(payDto.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(price.getCurrency())
                                                .setUnitAmount(price.getPrice())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(product.getName())
                                                                .setDescription(product.getDescription())
                                                                .build())
                                                .build())
                                .build())
                .build();

        product.setCurrentPrice(price);
        product.setStatus(ProductStatus.RESERVED);
        productRepository.save(product);

        return Session.create(params);
    }

    // TODO: Implement webhook for stripe
    @Override
    public void paidSuccess(String id) {
        transactionRepository.updateStatusByThirdPartyIdAndThirdPartyProvider(id, provider ,TransactionStatus.PAID.name());
        // TODO: Send notification to supplier
    }

    // TODO: Implement webhook for stripe
    @Override
    public void paidFailed(String id) {
        transactionRepository.updateStatusByThirdPartyIdAndThirdPartyProvider(id, provider, TransactionStatus.FAILED.name());
    }

    @Override
    public void processResult(Object payload, String secret) throws Exception {
        Event event = Webhook.constructEvent(
                (String) payload, secret, webhookSecret
        );

        switch (event.getType()) {
            case "checkout.session.async_payment_succeeded": {
                logger.info("Payment succeed");
                paidSuccess(event.getId());
                break;
            }
            case "checkout.session.async_payment_failed": {
                logger.info("Payment failed");
                paidFailed(event.getId());
                break;
            }
            default:
                logger.info("Unhandled event: " + event.getType());
        }
    }
}
