package com.oluwaseyi.in.Moneymanager.repository;

import com.oluwaseyi.in.Moneymanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
