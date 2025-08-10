package com.java.cloudexpensetrack.controller;

import com.java.cloudexpensetrack.dto.ExpenseDTO;
import com.java.cloudexpensetrack.model.Expense;
import com.java.cloudexpensetrack.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) { this.expenseService = expenseService; }

    @PostMapping
    public ResponseEntity<Expense> create(@Valid @RequestBody ExpenseDTO dto, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        Expense e = new Expense();
        e.setUserId(userId);
        e.setAmount(dto.getAmount());
        e.setCurrency(dto.getCurrency());
        e.setCategoryId(dto.getCategoryId());
        e.setDescription(dto.getDescription());
        e.setDate(dto.getDate() != null ? dto.getDate() : Instant.now());
        Expense created = expenseService.create(e);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> list(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(expenseService.list(userId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getOne(@PathVariable String id, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return (ResponseEntity<Expense>) expenseService.findById(id).map(e -> {
            if (!userId.equals(e.getUserId())) return ResponseEntity.<Expense>status(403).build();
            return ResponseEntity.ok(e);
        }).orElseGet(() -> ResponseEntity.<Expense>notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ExpenseDTO dto, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return expenseService.findById(id).map(e -> {
            if (!userId.equals(e.getUserId())) return ResponseEntity.<Expense>status(403).build();
            if (dto.getAmount() != null) e.setAmount(dto.getAmount());
            if (dto.getCurrency() != null) e.setCurrency(dto.getCurrency());
            if (dto.getCategoryId() != null) e.setCategoryId(dto.getCategoryId());
            if (dto.getDate() != null) e.setDate(dto.getDate());
            if (dto.getDescription() != null) e.setDescription(dto.getDescription());
            Expense saved = expenseService.update(e);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.<Expense>notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return expenseService.findById(id).map(e -> {
            if (!userId.equals(e.getUserId())) return ResponseEntity.status(403).build();
            expenseService.delete(id);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/between")
    public ResponseEntity<List<Expense>> between(@RequestParam String from, @RequestParam String to, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        Instant f = Instant.parse(from);
        Instant t = Instant.parse(to);
        return ResponseEntity.ok(expenseService.between(userId, f, t));
    }
}
