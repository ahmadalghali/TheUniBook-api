package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.Rating;
import com.greenwich.theunibook.repository.IdeaRepository;
import com.greenwich.theunibook.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RatingService {
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    IdeaRepository ideaRepository;

    public HashMap<String, Object> addLike(int ideaId, int userId) {


        HashMap<String, Object> addRatingResponse = new HashMap<>();

        try {
            Rating rating = ratingRepository.exists(userId, ideaId);
            //neutral state 0 - 0
            if (rating == null) {

                rating = new Rating(true, ideaId, userId);

                ratingRepository.save(rating);
                addRatingResponse.put("message", "liked");

            }
            // if liked - remove like
            else if (rating.getLiked()) {
                ratingRepository.delete(rating);
                //rating.setLiked();
                //ratingRepository.save(rating);
                addRatingResponse.put("message", "Like removed");
            }
            //
            else {

                rating.setLiked(true);
                ratingRepository.save(rating);
                addRatingResponse.put("message", "liked");

            }

            updateIdeaScore(ideaId);


        } catch (Exception e) {
            e.printStackTrace();
            addRatingResponse.put("message", "failed");
        }

        return addRatingResponse;

    }

    private void updateIdeaScore(int ideaId) {
        Idea idea = ideaRepository.findById(ideaId).get();

        int ideaLikes = ratingRepository.getIdeaLikes(idea.getId());
        int ideaDislikes = ratingRepository.getIdeaDislikes(idea.getId());

        idea.setScore(ideaLikes - ideaDislikes);
        ideaRepository.save(idea);
    }

    public HashMap<String, Object> disLike(int ideaId, int userId) {


        HashMap<String, Object> addRatingResponse = new HashMap<>();

        try {
            Rating rating = ratingRepository.exists(userId, ideaId);
            if (rating == null) {

                rating = new Rating(false, ideaId, userId);

                ratingRepository.save(rating);
                addRatingResponse.put("message", "disliked");
            } else if (rating.getLiked()) {
                rating.setLiked(false);
                ratingRepository.save(rating);
                addRatingResponse.put("message", "Disliked");
            } else {
                ratingRepository.delete(rating);
                ///rating.setLiked(false);
                //ratingRepository.save(rating);
                addRatingResponse.put("message", "Dislike removed");

            }

        } catch (Exception e) {
            e.printStackTrace();
            addRatingResponse.put("message", "failed");
        }

        return addRatingResponse;

    }


}
