package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Browser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrowserRepository extends CrudRepository<Browser, Integer> {
}
