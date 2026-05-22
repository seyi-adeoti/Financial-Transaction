package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.entity.Profile;
import com.oluwaseyi.in.Moneymanager.interfaces.BudgetService;
import com.oluwaseyi.in.Moneymanager.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget create(Budget budget) {
        logger.info("Creating budget for profile {} and category {}", budget.getProfile() == null ? null : budget.getProfile().getId(), budget.getCategory());
        return budgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> findByProfileAndCategory(Profile profile, String category) {
        return budgetRepository.findByProfileAndCategory(profile, category);
    }
}
