package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Idea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaRepository extends CrudRepository<Idea,Integer> {

}
