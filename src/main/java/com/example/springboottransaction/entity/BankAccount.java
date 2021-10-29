package com.example.springboottransaction.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "full_name", length = 45, nullable = false)
    private String fullName;
    @Column(name = "balance", nullable = false)
    private double balance;
}
