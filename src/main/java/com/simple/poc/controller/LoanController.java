package com.simple.poc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.poc.domain.Customer;
import com.simple.poc.domain.EmiPayment;
import com.simple.poc.domain.PaymentReference;
import com.simple.poc.domain.PersonalLoan;
import com.simple.poc.service.CustomerService;
import com.simple.poc.service.EmiPaymentService;
import com.simple.poc.service.LoanService;
import com.simple.poc.service.PaymentReferenceService;

@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;
    private final CustomerService customerService;
    private final EmiPaymentService emiPaymentService;
    private final PaymentReferenceService paymentReferenceService;

    public LoanController(LoanService loanService, CustomerService customerService,
                          EmiPaymentService emiPaymentService, PaymentReferenceService paymentReferenceService) {
        this.loanService = loanService;
        this.customerService = customerService;
        this.emiPaymentService = emiPaymentService;
        this.paymentReferenceService = paymentReferenceService;
    }

    @GetMapping("/loans/{loanNo}")
    public ResponseEntity<String> getLoanDetails(@PathVariable String loanNo) {
        try {
            loanService.fetchLoanDetailsAndWriteToFile(loanNo);
            return ResponseEntity.ok("Loan details fetched and written to file successfully for loan number: " + loanNo);
        } catch (com.simple.poc.exception.LoanNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
        }
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/loans")
    public ResponseEntity<?> saveLoan(@RequestBody PersonalLoan loan) {
        PersonalLoan savedLoan = loanService.saveLoan(loan);
        return new ResponseEntity<>(savedLoan, HttpStatus.CREATED);
    }

    @PostMapping("/emi-payments")
    public ResponseEntity<EmiPayment> saveEmiPayment(@RequestBody EmiPayment emiPayment) {
        EmiPayment savedEmiPayment = emiPaymentService.saveEmiPayment(emiPayment);
        return new ResponseEntity<>(savedEmiPayment, HttpStatus.CREATED);
    }

    @PostMapping("/payment-references")
    public ResponseEntity<PaymentReference> savePaymentReference(@RequestBody PaymentReference paymentReference) {
        PaymentReference savedPaymentReference = paymentReferenceService.savePaymentReference(paymentReference);
        return new ResponseEntity<>(savedPaymentReference, HttpStatus.CREATED);
    }
}