package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExpenseResponse", description = "Response object containing expense details")
public class ExpenseResponse {

    @Schema(description = "Expense unique identifier", example = "1")
    private Long id;

    @Schema(description = "Expense vendor or merchant", example = "Supermarket")
    private String vendor;

    @Schema(description = "Expense description", example = "Weekly grocery shopping")
    private String description;

    @Schema(description = "Transaction ID associated with the expense", example = "1")
    private Long transactionId;

    @Schema(description = "Profile ID associated with the expense", example = "1")
    private Long profileId;

    @Schema(description = "Category ID associated with the expense", example = "2")
    private Long categoryId;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long id, String vendor, String description, Long transactionId, Long profileId, Long categoryId) {
        this.id = id;
        this.vendor = vendor;
        this.description = description;
        this.transactionId = transactionId;
        this.profileId = profileId;
        this.categoryId = categoryId;
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
