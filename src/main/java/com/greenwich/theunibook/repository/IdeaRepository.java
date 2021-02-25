package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Idea;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends CrudRepository<Idea, Integer> {

    @Query("SELECT * FROM ideas")
    List<Idea> getIdeas();

    @Query("SELECT * FROM ideas WHERE department_id = :departmentId")
    List<Idea> getIdeasByDepartment(int departmentId);
}
