package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Integer> {


    @Query("SELECT * FROM ideas WHERE department_id = :departmentId")
    List<Idea> getIdeasByDepartmentId(int departmentId);

    //Page<Idea> findAllByDepartmentId(int departmentId, Pageable pageable);

    @Query("DECLARE @sortColumn VARCHAR(MAX) = :sortBy\n" +
            "DECLARE @PageNumber AS INT \n" +
            "DECLARE @RowsOfPage AS INT \n" +
            "DECLARE @CategoryId AS VARCHAR(MAX) = :categoryId\n" +
            "SET @PageNumber = :page\n" +
            "SET @RowsOfPage = 5 \n" +
            "SELECT * FROM ideas \n" +
            "WHERE department_id = :departmentId\n" +
            "AND id_category_ideas like @CategoryId\n" +
            "ORDER BY \n" +
            "CASE  \n" +
            "WHEN @sortColumn = 'latest' THEN date\n" +
            "END DESC,\n" +
            "CASE WHEN @sortColumn = 'most_viewed' THEN views \n" +
            "END DESC\n" +
            "OFFSET (@PageNumber-1)*@RowsOfPage ROWS\n" +
            "FETCH NEXT @RowsOfPage ROWS ONLY")
    List<Idea> getIdeas(int departmentId, int page, String sortBy, String categoryId);

    int countIdeasByDepartmentId(int departmentId);

}
