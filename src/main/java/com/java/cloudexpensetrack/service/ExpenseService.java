package com.java.cloudexpensetrack.service;

import com.java.cloudexpensetrack.model.Expense;
import com.java.cloudexpensetrack.repository.ExpenseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) { this.expenseRepository = expenseRepository; }

    public Expense create(Expense e) {
        Instant now = Instant.now();
        e.setCreatedAt(now);
        e.setUpdatedAt(now);
        return expenseRepository.save(e);
    }

    public List<Expense> list(String userId, int page, int size) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));
    }

    public Optional<Expense> findById(String id) { return expenseRepository.findById(id); }

    public Expense update(Expense e) {
        e.setUpdatedAt(Instant.now());
        return expenseRepository.save(e);
    }

    public void delete(String id) { expenseRepository.deleteById(id); }

    public List<Expense> between(String userId, Instant from, Instant to) {
        return expenseRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, from, to);
    }
}
