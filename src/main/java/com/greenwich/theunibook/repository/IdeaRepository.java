package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Integer> {

    @Query("select * from ideas where is_anonymous = 1")
    List<Idea> getAnonymousIdeas();

    @Query("SELECT i.* FROM ideas i\n" +
            "JOIN users u on i.id_users = u.id_users\n" +
            "where u.is_hidden = 0")
    List<Idea> getIdeas();

    @Query("SELECT * FROM ideas WHERE department_id = :departmentId")
    List<Idea> getIdeasByDepartmentId(int departmentId);

    //Page<Idea> findAllByDepartmentId(int departmentId, Pageable pageable);

    @Query("DECLARE @sortColumn VARCHAR(MAX) = :sortBy \n" +
            "DECLARE @PageNumber AS INT  \n" +
            "DECLARE @RowsOfPage AS INT  \n" +
            "DECLARE @CategoryId AS VARCHAR(MAX) = :categoryId \n" +
            "SET @PageNumber = :page \n" +
            "SET @RowsOfPage = 5  \n" +
            "SELECT i.* FROM ideas i\n" +
            "JOIN users u on i.id_users = u.id_users\n" +
            "WHERE i.department_id = :departmentId \n" +
            "AND i.id_category_ideas like @CategoryId \n" +
            "AND u.is_hidden = 0\n" +        // < - -  this line excludes hidden ideas
            "ORDER BY  \n" +
            "CASE   \n" +
            "WHEN @sortColumn = 'latest' THEN i.date \n" +
            "END DESC, \n" +
            "CASE WHEN @sortColumn = 'most_viewed' THEN i.views  \n" +
            "END DESC \n" +
            "OFFSET (@PageNumber-1)*@RowsOfPage ROWS \n" +
            "FETCH NEXT @RowsOfPage ROWS ONLY")
    List<Idea> getIdeas(int departmentId, int page, String sortBy, String categoryId);

    @Query("SELECT id_QA_coordinator FROM department WHERE id_department = :departmentId")
    int getQACoordinatorId(int departmentId);

    @Query("SELECT idea_title FROM ideas WHERE id_ideas = :id")
    String getIdeaTitle(int id);

    @Query("SELECT COUNT(id_ideas) FROM ideas WHERE department_id = :departmentId")
    int countIdeasByDepartmentId(int departmentId);

    @Query("SELECT COUNT(DISTINCT(id_users)) FROM ideas WHERE department_id = :departmentId")
    int getContributorsPerDepartment(int departmentId);

    @Query("SELECT Count(id_ideas)\n" +
            "FROM   ideas i \n" +
            "WHERE  NOT EXISTS (SELECT *\n" +
            "FROM   comments c\n" +
            "WHERE   c.id_ideas = i.id_ideas)")
    int getNumberOfIdeasWithNoComments();

    @Query("select count(id_ideas) from ideas where is_anonymous = 1")
    int countAllByAnonymousTrue();

    @Modifying
    @Query("INSERT INTO idea_closure_date (from_date, to_date) VALUES (:fromDate , :toDate) ")
    void setIdeaClosureDate(LocalDate fromDate, LocalDate toDate);

    @Modifying
    @Query("DELETE FROM idea_closure_date")
    void deleteExistingDates();

    @Query("SELECT TOP 1 from_date FROM idea_closure_date")
    String getFromDate();

    @Query("SELECT TOP 1 to_date FROM idea_closure_date")
    String getToDate();
}
