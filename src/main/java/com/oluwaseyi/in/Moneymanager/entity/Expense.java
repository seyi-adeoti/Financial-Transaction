package com.oluwaseyi.in.Moneymanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor is required")
    private String vendor;

    @NotBlank(message = "Description is required")
    private String description;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    @NotNull(message = "Transaction is required")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    @NotNull(message = "Profile is required")
    private Profile profile;

    public Expense() {
    }

    public Expense(String vendor, String description, Transaction transaction, Profile profile) {
        this.vendor = vendor;
        this.description = description;
        this.transaction = transaction;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
