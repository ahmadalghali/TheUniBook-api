package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.CommentDTO;
import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.CommentRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    private ModelMapper modelMapper = new ModelMapper();



    public HashMap<String, Object> postComment(Comment comment) {

        HashMap<String, Object> postCommentResponse = new HashMap<>();

        try {

            User commentAuthor = userRepository.findById(comment.getAuthorId()).get();

            if (!commentAuthor.isEnabled()) {
                postCommentResponse.put("message", "user account is disabled");
                return postCommentResponse;
            }

            Comment savedComment = commentRepository.save(comment);
            commentAuthor.setScore(commentAuthor.getScore() + 1);
            userRepository.save(commentAuthor);

            postCommentResponse.put("comment", convertToCommentDTO(savedComment));
            postCommentResponse.put("message", "comment saved");


        } catch (Exception e) {
            e.printStackTrace();
            postCommentResponse.put("message", "failed to save comment");
        }
        return postCommentResponse;
    }


    public HashMap<String, Object> notifyIdeaAuthorByEmail(Comment comment) {

        HashMap<String, Object> emailResponse = new HashMap<>();

        try {
            //Get the email of the author of the idea
            String ideaAuthorEmail = commentRepository.getIdeaAuthorEmail(comment.getIdeaId());


            int ideaAuthorId = commentRepository.getIdeaAuthorId(comment.getIdeaId());


            //Check if the commenter is the same author of the idea so you don't send an email to them
            if (comment.getAuthorId() == ideaAuthorId) {
                emailResponse.put("message", "email not sent - commenter is idea author");
                return emailResponse;
            }


            EmailValidator emailValidator = EmailValidator.getInstance();
            if (emailValidator.isValid(ideaAuthorEmail)) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setFrom("theunibook1@gmail.com");
                mail.setTo(ideaAuthorEmail);
                mail.setSubject("Comment Added to Post!");
                mail.setText("\nHi, " + commentRepository.getAuthorName(ideaAuthorId) + "\n\n\nYour Idea Post has received a comment " + comment.getDescription() + "\n\nclick here to check it out: \nhttps://www.theunibook.co.uk\nThanks,\nTheUniBook Team");
                this.sender.send(mail);
                emailResponse.put("message", "email sent");

            } else {
                emailResponse.put("message", "email invalid");
            }


        } catch (Exception e) {
            e.printStackTrace();
            emailResponse.put("message", "failed to send email");

        }

        return emailResponse;
    }


//    public List<Comment> getCommentsForIdea(int ideaId) {
//
//        List<Comment> comments = commentRepository.getAllByIdeaIdOrderByDateDesc(ideaId)
//                .stream()
//                .map(this::addAuthorName)
//                .collect(Collectors.toList());
//        return comments;
//    }

    public List<CommentDTO> getCommentsForIdea(int ideaId) {

        List<CommentDTO> commentDTOs = commentRepository.getAllByIdeaIdOrderByDateDesc(ideaId)
                .stream().map(this::convertToCommentDTO).collect(Collectors.toList());


        return commentDTOs;
    }

    private Comment addAuthorName(Comment comment) {

        User author = userRepository.findById(comment.getAuthorId()).get();

        comment.setAuthorName(author.getFirstname() + " " + author.getLastname());

        return comment;
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        User ideaAuthor = (userRepository.findById(comment.getAuthorId())).get();
        if (comment.isAnonymous()) {
            commentDTO.setAuthorName("Anonymous");
        } else {
            commentDTO.setAuthorName(ideaAuthor.getFirstname() + " " + ideaAuthor.getLastname());
        }

        return commentDTO;
    }
}
