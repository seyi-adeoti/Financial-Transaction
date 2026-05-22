package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Expense create(Expense expense);

    List<Expense> findAll();

    Optional<Expense> findById(Long id);

    Double sumAmountByProfileAndCategoryAndMonth(Long profileId, String category, int month, int year);
}
