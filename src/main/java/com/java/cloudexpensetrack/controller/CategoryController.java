package com.java.cloudexpensetrack.controller;

import com.java.cloudexpensetrack.model.Category;
import com.java.cloudexpensetrack.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping
    public ResponseEntity<List<Category>> list(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(categoryService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category c, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        c.setUserId(userId);
        Category created = categoryService.create(c);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id,
                                           @RequestBody Category incoming,
                                           Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return categoryService.findById(id).map(cat -> {
            if (!userId.equals(cat.getUserId())) {
                return ResponseEntity.status(403).<Category>build();
            }
            if (incoming.getName() != null) cat.setName(incoming.getName());
            if (incoming.getColor() != null) cat.setColor(incoming.getColor());
            Category saved = categoryService.create(cat);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return categoryService.findById(id).map(cat -> {
            if (!userId.equals(cat.getUserId())) return ResponseEntity.<Void>status(403).build();
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

}
