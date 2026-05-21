package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.Expense;
import com.oluwaseyi.in.Moneymanager.interfaces.ExpenseService;
import com.oluwaseyi.in.Moneymanager.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense create(Expense expense) {
        logger.info("Creating expense for transaction id: {}", expense.getTransaction().getId());
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> findAll() {
        logger.info("Retrieving all expenses");
        return expenseRepository.findAll();
    }

    @Override
    public Optional<Expense> findById(Long id) {
        logger.info("Retrieving expense with id: {}", id);
        return expenseRepository.findById(id);
    }
}
