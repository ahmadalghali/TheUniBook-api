package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Category;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("Select * from category_ideas")
    List<Category> getCategories();

    @Query("SELECT COUNT(id_ideas) FROM ideas where id_category_ideas = :categoryId")
    int getIdeaCountForCategory(@Param("categoryId") int categoryId);

}
