package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("comments")
public class Comment {

    @Id
    @Column("id_comment")
    private int id;

    @Column("comment_description")
    private String description;

    @Column("id_ideas")
    private int ideaId;

    @Column("id_users")
    private int authorId;

    @Column("is_anonymous")
    private boolean anonymous;

    @Transient
    private String authorName;


    private LocalDateTime date = LocalDateTime.now();

    public Comment(String description, int ideaId, int authorId) {
        this.description = description;
        this.ideaId = ideaId;
        this.authorId = authorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}
