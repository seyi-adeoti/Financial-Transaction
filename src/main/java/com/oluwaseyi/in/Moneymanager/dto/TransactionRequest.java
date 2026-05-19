package com.oluwaseyi.in.Moneymanager.dto;

import com.oluwaseyi.in.Moneymanager.validation.ValidCurrency;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Schema(name = "TransactionRequest", description = "Request object for creating or updating a transaction")
public class TransactionRequest {

    @Schema(description = "Transaction description", example = "Grocery shopping")
    @NotBlank(message = "Description must not be blank")
    private String description;

    @Schema(description = "Transaction amount", example = "250.50")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @Schema(description = "Currency code", example = "USD", pattern = "^(USD|EUR|GBP|NGN)$")
    @NotBlank(message = "Currency is required")
    @ValidCurrency
    private String currency;

    @Schema(description = "Transaction category", example = "Groceries")
    @NotBlank(message = "Category is required")
    private String category;

    @Schema(description = "Transaction type", example = "EXPENSE", allowableValues = {"INCOME", "EXPENSE"})
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @Schema(description = "Transaction date", example = "2026-05-19", type = "string", format = "date")
    @NotNull(message = "Transaction date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    public TransactionRequest() {
    }

    public TransactionRequest(String description, Double amount, String currency, String category,
                              TransactionType transactionType, LocalDate transactionDate) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
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
}
