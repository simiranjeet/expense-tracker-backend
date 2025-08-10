package com.java.cloudexpensetrack.repository;

import com.java.cloudexpensetrack.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserIdOrderByDateDesc(String userId, Pageable pageable);
    List<Expense> findByUserIdAndDateBetweenOrderByDateDesc(String userId, Instant from, Instant to);
}