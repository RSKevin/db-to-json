package com.simple.poc.service;

import com.simple.poc.domain.PaymentReference;
import com.simple.poc.repository.PaymentReferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentReferenceService {

    private final PaymentReferenceRepository paymentReferenceRepository;

    public PaymentReferenceService(PaymentReferenceRepository paymentReferenceRepository) {
        this.paymentReferenceRepository = paymentReferenceRepository;
    }

    public PaymentReference savePaymentReference(PaymentReference paymentReference) {
        return paymentReferenceRepository.save(paymentReference);
    }
}