package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Integer> {

    @Query("SELECT * FROM ideas")
    List<Idea> getIdeas();

    @Query("SELECT * FROM ideas WHERE department_id = :departmentId")
    List<Idea> getIdeasByDepartmentId(int departmentId);

    Page<Idea> findAllByDepartmentId(int departmentId, Pageable pageable);

    @Query("DECLARE @PageNumber AS INT\n" +
            "DECLARE @RowsOfPage AS INT\n" +
            "SET @PageNumber= :page\n" +
            "SET @RowsOfPage=5\n" +
            "SELECT * FROM ideas\n" +
            "WHERE department_id = :departmentId\n" +
            "ORDER BY DATE DESC\n" +
            "OFFSET (@PageNumber-1)*@RowsOfPage ROWS\n" +
            "FETCH NEXT @RowsOfPage ROWS ONLY")
    List<Idea> getIdeasByDepartmentIdPaginated(int departmentId, int page);

    int countIdeasByDepartmentId(int departmentId);

}
