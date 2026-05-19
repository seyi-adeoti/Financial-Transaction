package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.dto.TransactionRequest;
import com.oluwaseyi.in.Moneymanager.dto.TransactionResponse;
import com.oluwaseyi.in.Moneymanager.dto.TransactionSummaryResponse;
import com.oluwaseyi.in.Moneymanager.dto.TransactionType;
import com.oluwaseyi.in.Moneymanager.exception.ResourceNotFoundException;
import com.oluwaseyi.in.Moneymanager.interfaces.TransactionService;
import com.oluwaseyi.in.Moneymanager.mapper.TransactionMapper;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "API endpoints for managing financial transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Creates a new financial transaction with the provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        logger.info("Creating transaction with description: {}", request.getDescription());
        var transaction = transactionMapper.toEntity(request);
        var created = transactionService.create(transaction);
        var response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Transaction created successfully",
                transactionMapper.toResponse(created)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get transactions", description = "Retrieves transactions with optional filters for currency, type, category, date range, and description search")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid filter values"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, name = "description") String description) {
        logger.info("Fetching transactions with filters: currency={}, type={}, category={}, fromDate={}, toDate={}, description={}",
                currency, transactionType, category, fromDate, toDate, description);
        var transactions = transactionService.findAllByFilter(currency, transactionType, category, fromDate, toDate, description);
        var responses = transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
        var response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transactions retrieved successfully",
                responses
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get transaction summary", description = "Retrieves aggregated transaction totals and category breakdowns")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid filter values"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<TransactionSummaryResponse>> getTransactionSummary(
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, name = "description") String description) {
        logger.info("Fetching transaction summary with filters: currency={}, type={}, category={}, fromDate={}, toDate={}, description={}",
                currency, transactionType, category, fromDate, toDate, description);
        var summary = transactionService.getSummary(currency, transactionType, category, fromDate, toDate, description);
        var response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transaction summary retrieved successfully",
                summary
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(
            @Parameter(description = "Transaction ID", example = "1") @PathVariable Long id) {
        logger.info("Fetching transaction with id: {}", id);
        var transaction = transactionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        var response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transaction retrieved successfully",
                transactionMapper.toResponse(transaction)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transaction", description = "Updates an existing transaction with new details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(
            @Parameter(description = "Transaction ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        logger.info("Updating transaction with id: {}", id);
        var transaction = transactionMapper.toEntity(request);
        var updated = transactionService.update(id, transaction);
        var response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transaction updated successfully",
                transactionMapper.toResponse(updated)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction", description = "Deletes a transaction by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<?>> deleteTransaction(
            @Parameter(description = "Transaction ID", example = "1") @PathVariable Long id) {
        logger.info("Deleting transaction with id: {}", id);
        transactionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        transactionService.delete(id);
        var response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transaction deleted successfully"
        );
        return ResponseEntity.ok(response);
    }
}
