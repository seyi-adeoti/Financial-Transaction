package com.oluwaseyi.in.Moneymanager.mapper;

import com.oluwaseyi.in.Moneymanager.dto.ExpenseRequest;
import com.oluwaseyi.in.Moneymanager.dto.ExpenseResponse;
import com.oluwaseyi.in.Moneymanager.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseResponse toResponse(Expense entity) {
        return new ExpenseResponse(
                entity.getId(),
                entity.getVendor(),
                entity.getDescription(),
                entity.getTransaction().getId(),
                entity.getProfile().getId()
        );
    }

    public Expense toEntity(ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setVendor(request.getVendor());
        expense.setDescription(request.getDescription());
        return expense;
    }
}
