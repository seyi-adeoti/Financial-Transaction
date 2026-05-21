package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.dto.TransactionSummaryResponse;
import com.oluwaseyi.in.Moneymanager.dto.TransactionType;
import com.oluwaseyi.in.Moneymanager.entity.Transaction;
import com.oluwaseyi.in.Moneymanager.exception.ResourceNotFoundException;
import com.oluwaseyi.in.Moneymanager.interfaces.TransactionService;
import com.oluwaseyi.in.Moneymanager.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Transaction> findAllByFilter(String currency, TransactionType transactionType, String category,
                                             LocalDate fromDate, LocalDate toDate, String description) {
        logger.info("Retrieving transactions with filters: currency={}, type={}, category={}, fromDate={}, toDate={}, description={}",
                currency, transactionType, category, fromDate, toDate, description);

        if (currency == null && transactionType == null && category == null && fromDate == null && toDate == null && description == null) {
            return findAll();
        }

        Specification<Transaction> spec = Specification.where(null);

        if (currency != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("currency"), currency));
        }
        if (transactionType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionType"), transactionType));
        }
        if (category != null) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }
        if (fromDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("transactionDate"), fromDate));
        }
        if (toDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("transactionDate"), toDate));
        }
        if (description != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        return transactionRepository.findAll(spec);
    }

    @Override
    public TransactionSummaryResponse getSummary(String currency, TransactionType transactionType, String category,
                                                 LocalDate fromDate, LocalDate toDate, String description) {
        List<Transaction> filteredTransactions = findAllByFilter(currency, transactionType, category, fromDate, toDate, description);

        double totalAmount = filteredTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Double> totalIncomeByCurrency = filteredTransactions.stream()
                .filter(tx -> tx.getTransactionType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(Transaction::getCurrency,
                        Collectors.summingDouble(Transaction::getAmount)));

        Map<String, Double> totalExpenseByCurrency = filteredTransactions.stream()
                .filter(tx -> tx.getTransactionType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCurrency,
                        Collectors.summingDouble(Transaction::getAmount)));

        Map<String, Double> totalByCategory = filteredTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));

        return new TransactionSummaryResponse(
                filteredTransactions.size(),
                totalAmount,
                totalIncomeByCurrency,
                totalExpenseByCurrency,
                totalByCategory
        );
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
                    existing.setCategory(transaction.getCategory());
                    existing.setTransactionType(transaction.getTransactionType());
                    existing.setTransactionDate(transaction.getTransactionDate());
                    if (transaction.getProfile() != null) {
                        existing.setProfile(transaction.getProfile());
                    }
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
