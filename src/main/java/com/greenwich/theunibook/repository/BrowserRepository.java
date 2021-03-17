package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Browser;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrowserRepository extends CrudRepository<Browser, Integer> {

    @Query("SELECT TOP 10 FROM browsers ORDER BY times_used DESC")
    List<Browser> getAllBrowsers();

    @Query("SELECT * FROM browsers WHERE browser_name = :browserName")
    Browser findByName(String browserName);

}
