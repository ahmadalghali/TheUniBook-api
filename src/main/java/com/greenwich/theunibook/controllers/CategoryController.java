package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Category;
import com.greenwich.theunibook.repository.CategoryRepository;
import com.greenwich.theunibook.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }


    @DeleteMapping("/categories")
    public HashMap<String, Object> deleteCategoryById(@RequestParam int categoryId, @RequestParam int userId) {
        return categoryService.deleteCategoryById(categoryId, userId);
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestParam String name) {
        return categoryService.createCategory(name);
    }
}
