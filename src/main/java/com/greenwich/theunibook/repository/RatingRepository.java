package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.Rating;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {

    @Query("SELECT * FROM rating WHERE user_id = :userId AND id_idea = :ideaId")
    Rating exists(int userId, int ideaId);

    @Query("SELECT COUNT(id_rating) as likes from rating WHERE id_idea = :ideaId and rating = 1")
    int getIdeaLikes(int ideaId);

    @Query("SELECT COUNT(id_rating) as likes from rating WHERE id_idea = :ideaId and rating = 0")
    int getIdeaDislikes(int ideaId);

    @Query("SELECT id_idea FROM rating WHERE rating = 1 AND user_id = :userId")
    List<Integer> getLikedIdeasByUser(int userId);

    @Query("SELECT id_idea FROM rating WHERE rating = 0 AND user_id = :userId")
    List<Integer> getDislikedIdeasByUser(int userId);
}
