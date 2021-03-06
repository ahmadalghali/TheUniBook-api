package com.greenwich.theunibook.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Table("ideas")
public class Idea {
    @Column("id_ideas")
    @Id
    private int id;
    @Column("id_users")
    private int userId;
    @Column("id_category_ideas")
    private int categoryId;
    @Column("id_idea_status")
    private int statusId;

    @Column("department_id")
    private int departmentId;
    @Column("idea_title")
    private String title;
    @Column("idea_description")
    private String description;

    @Column("is_anonymous")
    private boolean isAnonymous;

    private int views;

    @Column("idea_document_path")
    private String documentPath;

    @Column("date")
    private LocalDateTime date; // = LocalDateTime.now();

    @Transient
    @JsonIgnore
    private MultipartFile document;

//    @Column("idea_likes")
//    private int ideaLikes;
//
//    @Column("idea_dislikes")
//    private int ideaDislikes;

    protected Idea() {

    }

    public Idea(int userId, int categoryId, String title, String description, String documentPath) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.documentPath = documentPath;
    }

    public Idea(int userId, int categoryId, int statusId, int departmentId, String title, String description, String documentPath) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.statusId = statusId;
        this.departmentId = departmentId;
        this.title = title;
        this.description = description;
        this.documentPath = documentPath;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }


//    public int getIdeaLikes() {
//        return ideaLikes;
//    }
//
//    public void setIdeaLikes(int ideaLikes) {
//        this.ideaLikes = ideaLikes;
//    }
//
//    public int getIdeaDislikes() {
//        return ideaDislikes;
//    }
//
//    public void setIdeaDislikes(int ideaDislikes) {
//        this.ideaDislikes = ideaDislikes;
//    }

}
