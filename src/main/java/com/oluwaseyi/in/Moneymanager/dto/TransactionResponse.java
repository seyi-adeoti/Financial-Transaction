package com.oluwaseyi.in.Moneymanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "TransactionResponse", description = "Response object containing transaction details")
public class TransactionResponse {

    @Schema(description = "Transaction unique identifier", example = "1")
    private Long id;

    @Schema(description = "Transaction description", example = "Grocery shopping")
    private String description;

    @Schema(description = "Transaction amount", example = "250.50")
    private Double amount;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Transaction category", example = "Groceries")
    private String category;

    @Schema(description = "Transaction type", example = "EXPENSE")
    private TransactionType transactionType;

    @Schema(description = "Transaction date", example = "2026-05-19", type = "string", format = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @Schema(description = "Profile details attached to the transaction")
    private ProfileResponse profile;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String description, Double amount, String currency,
                               String category, TransactionType transactionType, LocalDate transactionDate,
                               ProfileResponse profile) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public ProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(ProfileResponse profile) {
        this.profile = profile;
    }
}
