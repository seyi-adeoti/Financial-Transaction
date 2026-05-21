package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.dto.ExpenseRequest;
import com.oluwaseyi.in.Moneymanager.dto.ExpenseResponse;
import com.oluwaseyi.in.Moneymanager.entity.Expense;
import com.oluwaseyi.in.Moneymanager.entity.Profile;
import com.oluwaseyi.in.Moneymanager.entity.Transaction;
import com.oluwaseyi.in.Moneymanager.exception.ResourceNotFoundException;
import com.oluwaseyi.in.Moneymanager.interfaces.ExpenseService;
import com.oluwaseyi.in.Moneymanager.interfaces.ProfileService;
import com.oluwaseyi.in.Moneymanager.interfaces.TransactionService;
import com.oluwaseyi.in.Moneymanager.mapper.ExpenseMapper;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/expenses")
@Tag(name = "Expenses", description = "API endpoints for managing expense records tied to transactions and profiles")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;
    private final TransactionService transactionService;
    private final ProfileService profileService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService,
                             TransactionService transactionService,
                             ProfileService profileService,
                             ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.transactionService = transactionService;
        this.profileService = profileService;
        this.expenseMapper = expenseMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new expense", description = "Creates a new expense tied to a transaction and a profile")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@Valid @RequestBody ExpenseRequest request) {
        logger.info("Creating expense for transaction id: {}", request.getTransactionId());
        Transaction transaction = transactionService.findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + request.getTransactionId()));
        Profile profile = profileService.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Expense expense = expenseMapper.toEntity(request);
        expense.setTransaction(transaction);
        expense.setProfile(profile);

        var created = expenseService.create(expense);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Expense created successfully", expenseMapper.toResponse(created)));
    }

    @GetMapping
    @Operation(summary = "Get expenses", description = "Retrieves all expense records")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses() {
        logger.info("Fetching all expenses");
        var expenses = expenseService.findAll().stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Expenses retrieved successfully", expenses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID", description = "Retrieves an expense record by its ID")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(@PathVariable Long id) {
        logger.info("Fetching expense with id: {}", id);
        var expense = expenseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Expense retrieved successfully", expenseMapper.toResponse(expense)));
    }
}
