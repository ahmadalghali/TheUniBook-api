package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.CommentRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Properties;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender sender;


    public HashMap<String, Object> postComment(Comment comment) {

        HashMap<String, Object> postCommentResponse = new HashMap<>();

        try {

            Comment savedComment = commentRepository.save(comment);
            postCommentResponse.put("comment", savedComment);
            postCommentResponse.put("message", "comment saved");



            //Get the email of the author of the idea
            String ideaAuthorEmail = userRepository.getIdeaAuthorEmail(comment.getIdeaId());

            int ideaAuthorId = userRepository.getIdeaAuthorId(comment.getIdeaId());

             //Check if the commenter is the same author of the idea so you don't send an email to them
            if (comment.getAuthorId() != ideaAuthorId) {
                //Notify the author of the idea that a comment was left on their idea post
                if(!notifyIdeaAuthorByEmail(ideaAuthorEmail)) {
                    postCommentResponse.put("message", "failed to send email");
                };

            }

        } catch (Exception e) {
            e.printStackTrace();
            postCommentResponse.put("message", "failed to save comment or send email");
            //hello dude
        }
        return postCommentResponse;
    }

    private boolean notifyIdeaAuthorByEmail(String ideaAuthorEmail) {

        try {
            EmailValidator emailValidator = EmailValidator.getInstance();
            if(emailValidator.isValid(ideaAuthorEmail)) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setFrom("grefurniture@outlook.com");
                mail.setTo(ideaAuthorEmail);
                mail.setSubject("Comment Added to Post!");
                mail.setText("\n\nYour Idea Post has received a comment click here to check it out: \nhttps://theunibook.netlify.app/comments-2.html\nThanks,\nTheUniBook Team");
                this.sender.send(mail);
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Comment> getCommentsForIdea(int ideaId) {

        List<Comment> comments = commentRepository.getAllByIdeaIdOrderByDateDesc(ideaId)
                .stream()
                .map(this::addAuthorName)
                .collect(Collectors.toList());
        return comments;
    }

    private Comment addAuthorName(Comment comment) {

        User author = userRepository.findById(comment.getAuthorId()).get();

        comment.setAuthorName(author.getFirstname() + " " + author.getLastname());

        return comment;
    }
}
