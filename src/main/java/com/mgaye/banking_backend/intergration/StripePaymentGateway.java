// package com.mgaye.banking_backend.intergration;

// import org.springframework.stereotype.Service;

// // integration/StripePaymentGateway.java
// @Service
// @RequiredArgsConstructor
// public class StripePaymentGateway implements PaymentGatewayClient {
// private final StripeClient stripe;
// private final RetryTemplate retryTemplate;

// @Override
// public PaymentResult processCardPayment(CardPaymentRequest request) {
// return retryTemplate.execute(context -> {
// PaymentIntent intent = stripe.paymentIntents.create(
// new PaymentIntentCreateParams.Builder()
// .setAmount(request.amount().longValue())
// .setCurrency(request.currency())
// .setPaymentMethod(request.paymentMethodId())
// .build());
// return new PaymentResult(intent.getId(), intent.getStatus());
// });
// }
// }