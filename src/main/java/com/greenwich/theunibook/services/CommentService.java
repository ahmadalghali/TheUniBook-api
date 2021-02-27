package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;


    public HashMap<String, Object> postComment(Comment comment) {

        HashMap<String, Object> postCommentResponse = new HashMap<>();

        try {

            Comment savedComment = commentRepository.save(comment);

            postCommentResponse.put("comment", savedComment);
            postCommentResponse.put("message", "comment saved");


        } catch (Exception e) {
            e.printStackTrace();
            postCommentResponse.put("message", "failed to save comment");

        }

        return postCommentResponse;
    }

    public List<Comment> getCommentsForIdea(int ideaId) {

        return commentRepository.getAllByIdeaIdOrderByDateDesc(ideaId);
    }
}
