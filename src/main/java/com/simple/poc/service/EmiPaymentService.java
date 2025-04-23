package com.simple.poc.service;

import com.simple.poc.domain.EmiPayment;
import com.simple.poc.repository.EmiPaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class EmiPaymentService {

    private final EmiPaymentRepository emiPaymentRepository;

    public EmiPaymentService(EmiPaymentRepository emiPaymentRepository) {
        this.emiPaymentRepository = emiPaymentRepository;
    }

    public EmiPayment saveEmiPayment(EmiPayment emiPayment) {
        return emiPaymentRepository.save(emiPayment);
    }
}