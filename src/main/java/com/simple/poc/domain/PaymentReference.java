package com.simple.poc.domain;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReference implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@Temporal(TemporalType.DATE)
	private LocalDate date;

	private String modeOfPayment;
	private long amount;
	private String referenceNo;
	private String remarks;

	@ManyToOne
	@JoinColumn(name = "emi_id")
	//@JsonBackReference // Prevent infinite recursion
	private EmiPayment emiPayment;

}