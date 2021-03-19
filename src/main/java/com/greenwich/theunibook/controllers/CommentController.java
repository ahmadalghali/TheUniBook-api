package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.CommentDTO;
import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;


    @PostMapping("/comments")
    public HashMap<String, Object> postComment(@RequestBody Comment comment) {
        return commentService.postComment(comment);
    }

    @PostMapping("/comments/email")
    public HashMap<String, Object> notifyIdeaAuthorByEmail(@RequestBody Comment comment) {
        return commentService.notifyIdeaAuthorByEmail(comment);
    }

    @GetMapping("/comments")
    public List<CommentDTO> getCommentsForIdea(@RequestParam int ideaId) {
        return commentService.getCommentsForIdea(ideaId);
    }


}
