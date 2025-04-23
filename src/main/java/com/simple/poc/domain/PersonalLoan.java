package com.simple.poc.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalLoan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String loanNo;

	@NotNull
	@Column(unique = true)
	private String applicationNo;

	// Customer Relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	//@JsonManagedReference
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Customer customer;

	private String status;

	@NotNull
	private long amount;
	private String modeOfDisposal;
	private String referenceNo;
	private LocalDate disbursedOn;
	private int roi;
	private int serviceCharge;
	private long deduction;
	private long disbursedAmount;
	private double refund;
	private long overdueCharge;
	private double interest;
	private int totalTenure;
	private long emi;
	private long outstanding;
	private long due;
	private int remainingTenure;
	private LocalDate closedOn;

	@OneToMany(mappedBy = "personalLoan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Singular
	//@JsonManagedReference
	@JsonIgnoreProperties({"personalLoan"}) // Add this to prevent circular reference
	private List<EmiPayment> emiPayments;
	
}