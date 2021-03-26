package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.Category;
import com.greenwich.theunibook.repository.CategoryRepository;
import com.greenwich.theunibook.services.CategoryService;
import com.greenwich.theunibook.services.UserService;
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

    @Autowired
    UserService userService;

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }


    @DeleteMapping("/categories")
    public HashMap<String, Object> deleteCategoryById(@RequestParam String email, @RequestParam String password, @RequestParam int categoryId) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return categoryService.deleteCategoryById(categoryId);
        } else {
            response.put("message", "user has no authorization for this action");
            return response;
        }

//        return categoryService.deleteCategoryById(categoryId, userId);
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestParam String email, @RequestParam String password, @RequestParam String name) {

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return categoryService.createCategory(name);
        }
        return null;
    }
}
