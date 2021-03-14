package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("report_ideas")
public class Report {

    @Id
    private int id;

    @Column("user_id")
    private int userId;

    @Column("idea_id")
    private int ideaId;

    @Column("report_id")
    private int reportId;

    public Report(int userId, int ideaId, int reportId) {
        this.userId = userId;
        this.ideaId = ideaId;
        this.reportId = reportId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
