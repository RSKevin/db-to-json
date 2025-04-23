package com.simple.poc.repository;

import com.simple.poc.domain.PersonalLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalLoanRepository extends JpaRepository<PersonalLoan, Long> {
    Optional<PersonalLoan> findByLoanNo(String loanNo);
}