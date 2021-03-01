package com.greenwich.theunibook.services;

import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.Category;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.CategoryRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    public List<Category> getCategories() {
//        List<Category> category = new ArrayList<>();
//        categoryRepository.findAll().forEach(category::add);
//        return category;

        return categoryRepository.getCategories();
    }

    public HashMap<String, Object> deleteCategoryById(int categoryId, int userId) {


        HashMap<String, Object> deleteCategoryResponse = new HashMap<>();

        try {
            User user = userRepository.findById(userId).get();

            //check authorization

            if (user.getRole().equals(UserRole.MANAGER)) {

                if (!categoryRepository.existsById(categoryId)) {
                    deleteCategoryResponse.put("categoryId", categoryId);
                    deleteCategoryResponse.put("message", "no category found");
                    return deleteCategoryResponse;
                }

                // check if category has ideas or not

                if (categoryRepository.getIdeaCountForCategory(categoryId) == 0) {
                    categoryRepository.deleteById(categoryId);

                    deleteCategoryResponse.put("categoryId", categoryId);
                    deleteCategoryResponse.put("deleted-by", user.getFirstname() + " " + user.getLastname());
                    deleteCategoryResponse.put("message", "category deleted successfully");
                } else {
                    deleteCategoryResponse.put("categoryId", categoryId);
                    deleteCategoryResponse.put("message", "category cannot be deleted, ideas exist for this category");
                }
            } else {
                deleteCategoryResponse.put("categoryId", categoryId);
                deleteCategoryResponse.put("message", "user has no authorization for this action");
            }
        } catch (Exception e) {
            e.printStackTrace();

            deleteCategoryResponse.put("categoryId", categoryId);
            deleteCategoryResponse.put("message", "failed to delete category, something went wrong");
            deleteCategoryResponse.put("Error", e.getLocalizedMessage());

        }

        return deleteCategoryResponse;
    }

    public Category createCategory(String name) {

        return categoryRepository.save(new Category(name));
    }
}
