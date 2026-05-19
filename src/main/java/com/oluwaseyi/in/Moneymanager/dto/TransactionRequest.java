package com.oluwaseyi.in.Moneymanager.dto;

import com.oluwaseyi.in.Moneymanager.validation.ValidCurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    public TransactionRequest() {
    }

    public TransactionRequest(String description, Double amount, String currency) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
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
}
