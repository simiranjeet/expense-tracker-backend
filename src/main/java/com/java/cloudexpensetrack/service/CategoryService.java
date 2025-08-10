package com.java.cloudexpensetrack.service;

import com.java.cloudexpensetrack.model.Category;
import com.java.cloudexpensetrack.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    public Category create(Category c) { return categoryRepository.save(c); }
    public List<Category> findByUserId(String userId) { return categoryRepository.findByUserId(userId); }
    public Optional<Category> findById(String id) { return categoryRepository.findById(id); }
    public void deleteById(String id) { categoryRepository.deleteById(id); }
}
