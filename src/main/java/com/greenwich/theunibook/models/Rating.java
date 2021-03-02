package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Rating {

    @Id
    @Column("id_rating")
    private int id;
    @Column("rating")
    private Boolean isLiked;
    @Column("id_idea")
    private int ideaId;
    @Column("user_id")
    private int userId;

    public Rating(boolean isLiked, int ideaId, int userId) {
        this.isLiked = isLiked;
        this.ideaId = ideaId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
