package com.simple.poc.repository;

import com.simple.poc.domain.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {
    // You might not need specific methods here for this requirement
}