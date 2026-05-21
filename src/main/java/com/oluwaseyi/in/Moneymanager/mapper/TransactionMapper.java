package com.oluwaseyi.in.Moneymanager.mapper;

import com.oluwaseyi.in.Moneymanager.dto.ProfileResponse;
import com.oluwaseyi.in.Moneymanager.dto.TransactionRequest;
import com.oluwaseyi.in.Moneymanager.dto.TransactionResponse;
import com.oluwaseyi.in.Moneymanager.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequest request) {
        return new Transaction(
                request.getDescription(),
                request.getAmount(),
                request.getCurrency(),
                request.getCategory(),
                request.getTransactionType(),
                request.getTransactionDate()
        );
    }

    public TransactionResponse toResponse(Transaction entity) {
        ProfileResponse profileResponse = null;
        if (entity.getProfile() != null) {
            profileResponse = new ProfileResponse(
                    entity.getProfile().getId(),
                    entity.getProfile().getName(),
                    entity.getProfile().getEmail()
            );
        }
        return new TransactionResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCategory(),
                entity.getTransactionType(),
                entity.getTransactionDate(),
                profileResponse
        );
    }

    public void updateEntity(TransactionRequest request, Transaction entity) {
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
        entity.setCategory(request.getCategory());
        entity.setTransactionType(request.getTransactionType());
        entity.setTransactionDate(request.getTransactionDate());
    }
}
