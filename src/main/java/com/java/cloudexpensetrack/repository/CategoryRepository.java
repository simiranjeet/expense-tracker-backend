package com.java.cloudexpensetrack.repository;

import com.java.cloudexpensetrack.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUserId(String userId);
}
