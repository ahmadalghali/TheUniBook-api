package com.greenwich.theunibook.models;

import org.springframework.data.relational.core.mapping.Column;

import java.sql.Date;


public class Idea {
    @Column("id_ideas")
    private int Id;
    @Column("id_user")
    private int UserId;
    @Column("id_category_ideas")
    private int CategoryId;
    @Column("id_idea_status")
    private int StatusId;
    @Column("department_id")
    private int DepartmentId;
    @Column("idea_title")
    private String Title;
    @Column("idea_description")
    private String Description;
    @Column("idea_document_path")
    private String DocumentPath;
    @Column("date")
    private java.sql.Date Date;

    public Idea(int userId, int categoryId, int statusId, int departmentId, String title, String description, String documentPath, java.sql.Date date) {
        UserId = userId;
        CategoryId = categoryId;
        StatusId = statusId;
        DepartmentId = departmentId;
        Title = title;
        Description = description;
        DocumentPath = documentPath;
        Date = date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        DepartmentId = departmentId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDocumentPath() {
        return DocumentPath;
    }

    public void setDocumentPath(String documentPath) {
        DocumentPath = documentPath;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }
}
