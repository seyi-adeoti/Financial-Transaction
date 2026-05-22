package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.interfaces.BudgetService;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/budgets")
@Tag(name = "Budgets", description = "Manage monthly budgets per category")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    @Operation(summary = "Create budget")
    public ResponseEntity<ApiResponse<Budget>> create(@RequestBody Budget budget) {
        var created = budgetService.create(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(HttpStatus.CREATED.value(), "Budget created", created));
    }

    @GetMapping("/profile/{profileId}/category/{category}")
    @Operation(summary = "Get budget for profile and category")
    public ResponseEntity<ApiResponse<Budget>> getBudget(@PathVariable Long profileId, @PathVariable String category) {
        var optional = budgetService.findByProfileAndCategory(new com.oluwaseyi.in.Moneymanager.entity.Profile() {{ setId(profileId); }}, category);
        return optional.map(b -> ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Budget found", b)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Budget not found", null)));
    }
}
