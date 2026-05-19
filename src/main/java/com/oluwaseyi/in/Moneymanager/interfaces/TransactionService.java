package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.dto.TransactionSummaryResponse;
import com.oluwaseyi.in.Moneymanager.dto.TransactionType;
import com.oluwaseyi.in.Moneymanager.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> findAll();

    List<Transaction> findAllByFilter(String currency,
                                      TransactionType transactionType,
                                      String category,
                                      LocalDate fromDate,
                                      LocalDate toDate,
                                      String description);

    TransactionSummaryResponse getSummary(String currency,
                                          TransactionType transactionType,
                                          String category,
                                          LocalDate fromDate,
                                          LocalDate toDate,
                                          String description);

    Optional<Transaction> findById(Long id);

    Transaction update(Long id, Transaction transaction);

    void delete(Long id);
}
