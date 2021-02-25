package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Category;
import com.greenwich.theunibook.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getCategories(){
//        List<Category> category = new ArrayList<>();
//        categoryRepository.findAll().forEach(category::add);
//        return category;

        return categoryRepository.getCategories();
    }
}
