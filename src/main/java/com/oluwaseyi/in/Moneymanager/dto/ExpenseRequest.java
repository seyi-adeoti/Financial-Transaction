package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ExpenseRequest", description = "Request object for creating an expense tied to a transaction and profile")
public class ExpenseRequest {

    @Schema(description = "Expense vendor or merchant", example = "Supermarket")
    @NotBlank(message = "Vendor is required")
    private String vendor;

    @Schema(description = "Expense description", example = "Weekly grocery shopping")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Transaction ID associated with the expense", example = "1")
    @NotNull(message = "Transaction ID is required")
    private Long transactionId;

    @Schema(description = "Profile ID associated with the expense", example = "1")
    @NotNull(message = "Profile ID is required")
    private Long profileId;

    public ExpenseRequest() {
    }

    public ExpenseRequest(String vendor, String description, Long transactionId, Long profileId) {
        this.vendor = vendor;
        this.description = description;
        this.transactionId = transactionId;
        this.profileId = profileId;
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
}
