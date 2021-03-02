package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Rating;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
    @Query("SELECT rating FROM rating WHERE user_id = :userId AND id_idea = :ideaId")
    boolean userHasLiked(int userId, int ideaId);


    @Query("SELECT * FROM rating WHERE user_id = :userId AND id_idea = :ideaId")
    Rating exists(int userId, int ideaId);

    @Query("SELECT COUNT(id_rating) as likes from rating WHERE id_idea = :ideaId and rating = 1")
    int getIdeaLikes(int ideaId);

    @Query("SELECT COUNT(id_rating) as likes from rating WHERE id_idea = :ideaId and rating = 0")
    int getIdeaDislikes(int ideaId);
}
