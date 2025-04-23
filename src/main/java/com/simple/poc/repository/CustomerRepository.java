package com.simple.poc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.poc.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerNo(String customerNo);
    Optional<Customer> findByMobileNo(String mobileNo);
}