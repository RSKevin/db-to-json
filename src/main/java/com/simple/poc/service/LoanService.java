package com.simple.poc.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.poc.domain.Customer;
import com.simple.poc.domain.EmiPayment;
import com.simple.poc.domain.PaymentReference;
import com.simple.poc.domain.PersonalLoan;
import com.simple.poc.exception.LoanNotFoundException;
import com.simple.poc.repository.CustomerRepository;
import com.simple.poc.repository.PersonalLoanRepository;

@Service
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final PersonalLoanRepository personalLoanRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Value("${output.file.path}")
    private String outputFilePath;

    public LoanService(PersonalLoanRepository personalLoanRepository, CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.personalLoanRepository = personalLoanRepository;
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PersonalLoan saveLoan(PersonalLoan loan) {
        // Handle Customer creation/retrieval
        if (loan.getCustomer() != null) {
            Customer existingCustomer = customerRepository.findByCustomerNo(loan.getCustomer().getCustomerNo()).orElse(null);
            if (existingCustomer != null) {
                loan.setCustomer(existingCustomer);
            } else {
                customerRepository.findByMobileNo(loan.getCustomer().getMobileNo())
                        .ifPresent(existingCust -> {
                            throw new DataIntegrityViolationException(
                                    "Customer with mobile number " + loan.getCustomer().getMobileNo() + " already exists."
                            );
                        });
                Customer savedCustomer = customerRepository.save(loan.getCustomer());
                loan.setCustomer(savedCustomer);
            }
        }

        // Save the loan itself
        PersonalLoan savedLoan = personalLoanRepository.save(loan);

        // Handle EMI Payments
        if (loan.getEmiPayments() != null) {
            for (EmiPayment emiPayment : loan.getEmiPayments()) {
                emiPayment.setPersonalLoan(savedLoan);
                // Payment References will be cascaded due to CascadeType.ALL in EmiPayment
                if (emiPayment.getPaymentReferences() != null) {
                    for (PaymentReference paymentReference : emiPayment.getPaymentReferences()) {
                        paymentReference.setEmiPayment(emiPayment);
                    }
                }
            }
            // EmiPaymentRepository will handle saving the EMIs due to CascadeType.ALL in PersonalLoan
        }

        return savedLoan;
    }

    @Transactional(readOnly = true) // Add this annotation
    public void fetchLoanDetailsAndWriteToFile(String loanNo) {
        logger.info("Fetching details for loan number: {}", loanNo);
        PersonalLoan loan = personalLoanRepository.findByLoanNo(loanNo)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with loan number: " + loanNo));
        
        // Force initialization of lazy-loaded collections
        Hibernate.initialize(loan.getCustomer());
        Hibernate.initialize(loan.getEmiPayments());
        if (loan.getEmiPayments() != null) {
            for (EmiPayment emi : loan.getEmiPayments()) {
                Hibernate.initialize(emi.getPaymentReferences());
            }
        }
        
        writeLoanObjectToJsonFile(loan);
        logger.info("Loan object written to JSON file for loan number: {}", loanNo);
    }

    private void writeLoanObjectToJsonFile(PersonalLoan loan) {
        try (FileWriter writer = new FileWriter(generateJsonFileName(outputFilePath, loan.getLoanNo()))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, loan);
        } catch (IOException e) {
            logger.error("Error writing PersonalLoan object to JSON file for loan number: {}", loan.getLoanNo(), e);
            throw new RuntimeException("Error writing loan details to JSON file.", e);
        }
    }

    private String generateJsonFileName(String filePath, String loanNo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return String.format("%s/loan_data_%s_%s.json", filePath, loanNo, timestamp);
    }
}

/*
package com.simple.poc.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.poc.domain.Customer;
import com.simple.poc.domain.EmiPayment;
import com.simple.poc.domain.PaymentReference;
import com.simple.poc.domain.PersonalLoan;
import com.simple.poc.exception.LoanNotFoundException;
import com.simple.poc.repository.CustomerRepository;
import com.simple.poc.repository.PersonalLoanRepository;

@Service
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final PersonalLoanRepository personalLoanRepository;
    private final CustomerRepository customerRepository;

    @Value("${output.file.path}")
    private String outputFilePath;

    public LoanService(PersonalLoanRepository personalLoanRepository, CustomerRepository customerRepository) {
        this.personalLoanRepository = personalLoanRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public PersonalLoan saveLoan(PersonalLoan loan) {
        // Handle Customer creation/retrieval
        if (loan.getCustomer() != null) {
            Customer existingCustomer = customerRepository.findByCustomerNo(loan.getCustomer().getCustomerNo()).orElse(null);
            if (existingCustomer != null) {
                loan.setCustomer(existingCustomer);
            } else {
                customerRepository.findByMobileNo(loan.getCustomer().getMobileNo())
                        .ifPresent(existingCust -> {
                            throw new DataIntegrityViolationException(
                                    "Customer with mobile number " + loan.getCustomer().getMobileNo() + " already exists."
                            );
                        });
                Customer savedCustomer = customerRepository.save(loan.getCustomer());
                loan.setCustomer(savedCustomer);
            }
        }

        // Save the loan itself
        PersonalLoan savedLoan = personalLoanRepository.save(loan);

        // Handle EMI Payments
        if (loan.getEmiPayments() != null) {
            for (EmiPayment emiPayment : loan.getEmiPayments()) {
                emiPayment.setPersonalLoan(savedLoan);
                // Payment References will be cascaded due to CascadeType.ALL in EmiPayment
                if (emiPayment.getPaymentReferences() != null) {
                    for (PaymentReference paymentReference : emiPayment.getPaymentReferences()) {
                        paymentReference.setEmiPayment(emiPayment);
                    }
                }
            }
            // EmiPaymentRepository will handle saving the EMIs due to CascadeType.ALL in PersonalLoan
        }

        return savedLoan;
    }

    public void fetchLoanDetailsAndWriteToFile(String loanNo) {
        logger.info("Fetching details for loan number: {}", loanNo);
        PersonalLoan loan = personalLoanRepository.findByLoanNo(loanNo)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with loan number: " + loanNo));

        writeToFile(loan);
        logger.info("Loan details written to file for loan number: {}", loanNo);
    }

    private void writeToFile(PersonalLoan loan) {
        try (FileWriter writer = new FileWriter(generateFileName(outputFilePath, loan.getLoanNo()))) {
            writer.write("Loan Details:\n");
            writer.write("------------------------------------\n");
            writer.write("Loan Number: " + loan.getLoanNo() + "\n");
            writer.write("Application Number: " + loan.getApplicationNo() + "\n");
            if (loan.getCustomer() != null) {
                writer.write("Customer Info:\n");
                writer.write("  Customer Number: " + loan.getCustomer().getCustomerNo() + "\n");
                writer.write("  First Name: " + loan.getCustomer().getFirstName() + "\n");
                writer.write("  Last Name: " + loan.getCustomer().getLastName() + "\n");
                writer.write("  City: " + loan.getCustomer().getCity() + "\n");
                writer.write("  Mobile: " + loan.getCustomer().getMobileNo() + "\n");
                writer.write("  REP: " + loan.getCustomer().isRep() + "\n");
            } else {
                writer.write("Customer Info: Not Available\n");
            }

            writer.write("Status: " + loan.getStatus() + "\n");
            writer.write("Loan Amount: " + loan.getAmount() + "\n");
            writer.write("Disbursed On: " + loan.getDisbursedOn() + "\n");
            writer.write("Total Tenure: " + loan.getTotalTenure() + "\n");
            writer.write("EMI Amount: " + loan.getEmi() + "\n");
            writer.write("\n");

            if (loan.getEmiPayments() != null && !loan.getEmiPayments().isEmpty()) {
                writer.write("EMI Payment Details:\n");
                writer.write("------------------------------------\n");
                for (EmiPayment emi : loan.getEmiPayments()) {
                    writer.write("  Instalment: " + emi.getInstalment() + "\n");
                    writer.write("  Due Date: " + emi.getDueDate() + "\n");
                    writer.write("  EMI Amount: " + emi.getEmi() + "\n");
                    writer.write("  Status: " + emi.getStatus() + "\n");
                    writer.write("  Payment Date: " + emi.getDate() + "\n");
                    writer.write("  Amount Paid: " + emi.getAmount() + "\n");
                    if (emi.getPaymentReferences() != null && !emi.getPaymentReferences().isEmpty()) {
                        writer.write("    Payment References:\n");
                        for (PaymentReference paymentReference : emi.getPaymentReferences()) {
                            writer.write("      Date: " + paymentReference.getDate() + "\n");
                            writer.write("      Mode: " + paymentReference.getModeOfPayment() + "\n");
                            writer.write("      Amount: " + paymentReference.getAmount() + "\n");
                            writer.write("      Reference No: " + paymentReference.getReferenceNo() + "\n");
                            writer.write("      Remarks: " + paymentReference.getRemarks() + "\n");
                        }
                    } else {
                        writer.write("    No Payment References found for this EMI.\n");
                    }
                    writer.write("  ------------------------------------\n");
                }
            } else {
                writer.write("No EMI Payment details found for this loan.\n");
            }

        } catch (IOException e) {
            logger.error("Error writing to file for loan number: {}", loan.getLoanNo(), e);
            throw new RuntimeException("Error writing loan details to file.", e);
        }
    }

    private String generateFileName(String filePath, String loanNo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return String.format("%s/loan_details_%s_%s.txt", filePath, loanNo, timestamp);
    }
}*/