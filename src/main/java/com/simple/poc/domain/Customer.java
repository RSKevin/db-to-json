package com.simple.poc.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "mobileNo" }) })
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String customerNo;
    private String firstName;
    private String lastName;
    private String city;

    @Column(unique = true)
    private String mobileNo;
    private boolean rep;

    @OneToMany(mappedBy = "customer")
    @Singular
    @JsonBackReference
    private List<PersonalLoan> personalLoans;
}