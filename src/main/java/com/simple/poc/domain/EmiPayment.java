package com.simple.poc.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "emi_payment", uniqueConstraints = { @UniqueConstraint(columnNames = { "loanNo", "dueDate"}) })
public class EmiPayment implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String loanNo;

	@NotNull
	private int instalment;

	private LocalDate dueDate;

	private long emi;

	private String status;

	private LocalDate date;

	private long amount;

	private double interest;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "emi_id")
	@Singular
	//@JsonManagedReference // Allow serialization of payment references
	@JsonIgnoreProperties({"emiPayment"}) // Add this
    private List<PaymentReference> paymentReferences;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    //@JsonBackReference // Prevent infinite recursion
    private PersonalLoan personalLoan;
}