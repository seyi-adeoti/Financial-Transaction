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
import com.oluwaseyi.in.Moneymanager.interfaces.BudgetService;
import com.oluwaseyi.in.Moneymanager.interfaces.CategoryService;
import com.oluwaseyi.in.Moneymanager.interfaces.NotificationService;
import org.springframework.web.multipart.MultipartFile;
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
import org.springframework.web.bind.annotation.RequestParam;
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
        private final CategoryService categoryService;
        private final BudgetService budgetService;
        private final NotificationService notificationService;

        public ExpenseController(ExpenseService expenseService,
                                                         TransactionService transactionService,
                                                         ProfileService profileService,
                                                         ExpenseMapper expenseMapper,
                                                         CategoryService categoryService,
                                                         BudgetService budgetService,
                                                         NotificationService notificationService) {
                this.expenseService = expenseService;
                this.transactionService = transactionService;
                this.profileService = profileService;
                this.expenseMapper = expenseMapper;
                this.categoryService = categoryService;
                this.budgetService = budgetService;
                this.notificationService = notificationService;
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
                if (request.getCategoryId() != null) {
                        categoryService.findById(request.getCategoryId()).ifPresent(expense::setCategory);
                }

        var created = expenseService.create(expense);

                // Budget check: compare monthly total with budget and notify if exceeded
                try {
                        String categoryName = created.getCategory() != null ? created.getCategory().getName() : created.getTransaction().getCategory();
                        java.time.LocalDate txDate = created.getTransaction().getTransactionDate();
                        int month = txDate.getMonthValue();
                        int year = txDate.getYear();
                        Double total = expenseService.sumAmountByProfileAndCategoryAndMonth(profile.getId(), categoryName, month, year);
                        budgetService.findByProfileAndCategory(profile, categoryName).ifPresent(budget -> {
                                if (total != null && total > budget.getAmount()) {
                                        notificationService.notifyBudgetExceeded(profile, budget, total);
                                }
                        });
                } catch (Exception ex) {
                        logger.warn("Budget check/notification failed: {}", ex.getMessage());
                }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Expense created successfully", expenseMapper.toResponse(created)));
    }

        @PostMapping("/import")
        @Operation(summary = "Import expenses from CSV")
        public ResponseEntity<ApiResponse<String>> importCsv(@RequestParam("file") MultipartFile file) {
                if (file.isEmpty()) {
                        return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "File is empty", null));
                }
                try (var in = new java.io.BufferedReader(new java.io.InputStreamReader(file.getInputStream()))) {
                        String line;
                        boolean first = true;
                        int createdCount = 0;
                        while ((line = in.readLine()) != null) {
                                if (first) { first = false; continue; } // skip header
                                var parts = line.split(",");
                                if (parts.length < 4) continue;
                                String vendor = parts[0].trim();
                                String description = parts[1].trim();
                                Long transactionId = Long.parseLong(parts[2].trim());
                                Long profileId = Long.parseLong(parts[3].trim());
                                Long categoryId = parts.length >= 5 && !parts[4].isBlank() ? Long.parseLong(parts[4].trim()) : null;

                                Transaction transaction = transactionService.findById(transactionId).orElse(null);
                                Profile profile = profileService.findById(profileId).orElse(null);
                                if (transaction == null || profile == null) continue;

                                Expense exp = new Expense();
                                exp.setVendor(vendor);
                                exp.setDescription(description);
                                exp.setTransaction(transaction);
                                exp.setProfile(profile);
                                if (categoryId != null) categoryService.findById(categoryId).ifPresent(exp::setCategory);
                                expenseService.create(exp);
                                createdCount++;
                        }
                        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Imported " + createdCount + " expenses", null));
                } catch (Exception ex) {
                        logger.error("Failed to import CSV", ex);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Import failed", null));
                }
        }

        @GetMapping("/export")
        @Operation(summary = "Export expenses as CSV")
        public ResponseEntity<String> exportCsv() {
                var expenses = expenseService.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("id,vendor,description,transactionId,profileId,category,amount,currency,transactionDate\n");
                for (Expense e : expenses) {
                        String categoryName = e.getCategory() != null ? e.getCategory().getName() : (e.getTransaction() != null ? e.getTransaction().getCategory() : "");
                        sb.append(e.getId()).append(',')
                                        .append(escape(e.getVendor())).append(',')
                                        .append(escape(e.getDescription())).append(',')
                                        .append(e.getTransaction() != null ? e.getTransaction().getId() : "").append(',')
                                        .append(e.getProfile() != null ? e.getProfile().getId() : "").append(',')
                                        .append(escape(categoryName)).append(',')
                                        .append(e.getTransaction() != null ? e.getTransaction().getAmount() : "").append(',')
                                        .append(e.getTransaction() != null ? e.getTransaction().getCurrency() : "").append(',')
                                        .append(e.getTransaction() != null && e.getTransaction().getTransactionDate() != null ? e.getTransaction().getTransactionDate().toString() : "").append('\n');
                }
                return ResponseEntity.ok()
                                .header("Content-Disposition", "attachment; filename=expenses.csv")
                                .header("Content-Type", "text/csv")
                                .body(sb.toString());
        }

        private String escape(String s) {
                if (s == null) return "";
                return s.replace("\n", " ").replace(",", " ");
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
