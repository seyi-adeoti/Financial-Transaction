package com.oluwaseyi.in.Moneymanager.repository;

import com.oluwaseyi.in.Moneymanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	@Query("SELECT COALESCE(SUM(e.transaction.amount), 0) FROM Expense e WHERE e.profile.id = :profileId AND e.transaction.category = :category AND MONTH(e.transaction.transactionDate) = :month AND YEAR(e.transaction.transactionDate) = :year")
	Double sumAmountByProfileAndCategoryAndMonth(@Param("profileId") Long profileId,
												  @Param("category") String category,
												  @Param("month") int month,
												  @Param("year") int year);

}
