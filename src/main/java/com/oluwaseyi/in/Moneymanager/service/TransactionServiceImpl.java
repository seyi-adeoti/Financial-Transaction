package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.Transaction;
import com.oluwaseyi.in.Moneymanager.exception.ResourceNotFoundException;
import com.oluwaseyi.in.Moneymanager.interfaces.TransactionService;
import com.oluwaseyi.in.Moneymanager.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {
        logger.info("Creating new transaction with description: {}", transaction.getDescription());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        logger.info("Retrieving all transactions");
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        logger.info("Retrieving transaction with id: {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        logger.info("Updating transaction with id: {}", id);
        return transactionRepository.findById(id)
                .map(existing -> {
                    existing.setDescription(transaction.getDescription());
                    existing.setAmount(transaction.getAmount());
                    existing.setCurrency(transaction.getCurrency());
                    return transactionRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting transaction with id: {}", id);
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
