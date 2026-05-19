package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(name = "TransactionSummaryResponse", description = "Aggregated transaction summary data")
public class TransactionSummaryResponse {

    @Schema(description = "Total number of transactions", example = "12")
    private long totalTransactions;

    @Schema(description = "Total amount for all transactions", example = "1290.50")
    private Double totalAmount;

    @Schema(description = "Total amount for income transactions by currency")
    private Map<String, Double> totalIncomeByCurrency;

    @Schema(description = "Total amount for expense transactions by currency")
    private Map<String, Double> totalExpenseByCurrency;

    @Schema(description = "Total amount by category")
    private Map<String, Double> totalByCategory;

    public TransactionSummaryResponse() {
    }

    public TransactionSummaryResponse(long totalTransactions, Double totalAmount,
                                      Map<String, Double> totalIncomeByCurrency,
                                      Map<String, Double> totalExpenseByCurrency,
                                      Map<String, Double> totalByCategory) {
        this.totalTransactions = totalTransactions;
        this.totalAmount = totalAmount;
        this.totalIncomeByCurrency = totalIncomeByCurrency;
        this.totalExpenseByCurrency = totalExpenseByCurrency;
        this.totalByCategory = totalByCategory;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Map<String, Double> getTotalIncomeByCurrency() {
        return totalIncomeByCurrency;
    }

    public void setTotalIncomeByCurrency(Map<String, Double> totalIncomeByCurrency) {
        this.totalIncomeByCurrency = totalIncomeByCurrency;
    }

    public Map<String, Double> getTotalExpenseByCurrency() {
        return totalExpenseByCurrency;
    }

    public void setTotalExpenseByCurrency(Map<String, Double> totalExpenseByCurrency) {
        this.totalExpenseByCurrency = totalExpenseByCurrency;
    }

    public Map<String, Double> getTotalByCategory() {
        return totalByCategory;
    }

    public void setTotalByCategory(Map<String, Double> totalByCategory) {
        this.totalByCategory = totalByCategory;
    }
}
