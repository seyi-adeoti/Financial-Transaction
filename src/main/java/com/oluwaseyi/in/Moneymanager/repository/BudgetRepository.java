package com.oluwaseyi.in.Moneymanager.repository;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByProfileAndCategory(Profile profile, String category);
}
