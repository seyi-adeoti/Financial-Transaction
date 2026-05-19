package com.oluwaseyi.in.Moneymanager.mapper;

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
                request.getCurrency()
        );
    }

    public TransactionResponse toResponse(Transaction entity) {
        return new TransactionResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getCurrency()
        );
    }

    public void updateEntity(TransactionRequest request, Transaction entity) {
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
    }
}
