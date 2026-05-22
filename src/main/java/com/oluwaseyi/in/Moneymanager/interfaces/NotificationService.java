package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.entity.Profile;

public interface NotificationService {
    void notifyBudgetExceeded(Profile profile, Budget budget, Double total);
}
