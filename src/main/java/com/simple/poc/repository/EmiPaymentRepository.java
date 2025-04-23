package com.simple.poc.repository;

import com.simple.poc.domain.EmiPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {
    // You might not need specific methods here for this requirement
}