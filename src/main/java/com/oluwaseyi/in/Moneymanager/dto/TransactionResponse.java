package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String description, Double amount, String currency) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
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
}
