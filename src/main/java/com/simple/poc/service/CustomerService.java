package com.simple.poc.service;

import org.springframework.stereotype.Service;

import com.simple.poc.domain.Customer;
import com.simple.poc.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}