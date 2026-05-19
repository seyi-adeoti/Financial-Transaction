package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> findAll();

    Optional<Transaction> findById(Long id);

    Transaction update(Long id, Transaction transaction);

    void delete(Long id);
}
