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
            String authorEmail = commentRepository.getIdeaAuthorEmail(comment.getIdeaId());
            //Notify the author of the idea that a comment was left on their idea post
            notifyIdeaAuthorByEmail(authorEmail);

        } catch (Exception e) {
            e.printStackTrace();
            postCommentResponse.put("message", "failed to save comment or send email");

        }

        return postCommentResponse;
    }

    private boolean notifyIdeaAuthorByEmail(String ideaAuthorEmail) throws MessagingException {

        EmailValidator emailValidator = EmailValidator.getInstance();

        if(emailValidator.isValid(ideaAuthorEmail)) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("grefurniture@outlook.com");
            mail.setTo("a@hot.com");
            mail.setSubject("Testing Email Service");
            mail.setText("Test email content");
            this.sender.send(mail);

            return true;
        }

        return false;

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
