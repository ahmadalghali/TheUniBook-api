package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Rating;
import com.greenwich.theunibook.models.Report;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {

    @Query("SELECT * FROM report_ideas WHERE user_id = :userId AND idea_id = :ideaId")
    Report exists(int userId, int ideaId);

    @Modifying
    @Query("UPDATE report_ideas SET report_id = :reportId WHERE user_id = :userId AND idea_id = :ideaId")
    void updateReportedIdea(int reportId, int userId, int ideaId);

    @Query("SELECT idea_id FROM report_ideas WHERE user_id = :userId")
    List<Integer> getReportedIdeasByUser(int userId);

    @Modifying
    @Query("DELETE FROM report_ideas WHERE user_id=:userId AND idea_id=:ideaId")
    void removeIdeaReport(int userId, int ideaId);
}
