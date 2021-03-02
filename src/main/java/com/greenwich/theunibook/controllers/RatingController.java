package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PostMapping("/ideas/like")
    public HashMap<String, Object> addLike(@RequestParam int ideaId, @RequestParam int userId) {
        return ratingService.addLike(ideaId, userId);
    }

    @PostMapping("/ideas/dislike")
    public HashMap<String, Object> disLike(@RequestParam int ideaId, @RequestParam int userId) {
        return ratingService.disLike(ideaId, userId);
    }

}
