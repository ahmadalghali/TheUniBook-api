package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Pages;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<Pages, Integer> {

    @Query("SELECT * FROM pages ORDER BY page_views DESC")
    List<Pages> getAllPagesByViews();


}
