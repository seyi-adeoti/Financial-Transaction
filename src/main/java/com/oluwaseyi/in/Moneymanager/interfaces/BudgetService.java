package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.entity.Profile;

import java.util.Optional;

public interface BudgetService {
    Budget create(Budget budget);
    Optional<Budget> findByProfileAndCategory(Profile profile, String category);
}
