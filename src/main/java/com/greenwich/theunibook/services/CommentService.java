package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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

            //Get the email of the author of the idea
            String authorEmail = commentRepository.getIdeaAuthorEmail(comment.getIdeaId());
            //Notify the author of the idea that a comment was left on their idea post
            notifyIdeaAuthorByEmail(authorEmail);

        } catch (Exception e) {
            e.printStackTrace();
            postCommentResponse.put("message", "failed to save comment");

        }

        return postCommentResponse;
    }

    private boolean notifyIdeaAuthorByEmail(String ideaAuthorEmail) throws MessagingException {

    return true;
//        try {
//
//            Properties prop = new Properties();
//            prop.put("mail.smtp.auth", true);
//            prop.put("mail.smtp.starttls.enable", "true");
//            prop.put("mail.smtp.host", "smtp.mailtrap.io");
//            prop.put("mail.smtp.port", "25");
//            prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
//
//            String senderEmail = "theunibook@hotmail.com";
//            String password = "webdev3778";
//
//            Session session = Session.getInstance(prop, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//
//                    return new PasswordAuthentication(senderEmail, password);
//                }
//            });
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(senderEmail));
//            message.setRecipients(
//                    Message.RecipientType.TO, InternetAddress.parse(ideaAuthorEmail));
//            message.setSubject("Comment Added to your post!");
//
//            String msg = "You have received a comment on your idea post, click here to check it out :\n" +
//                    " https://theunibook.netlify.app";
//
//            MimeBodyPart mimeBodyPart = new MimeBodyPart();
//            mimeBodyPart.setContent(msg, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(mimeBodyPart);
//
//            message.setContent(multipart);
//
//            Transport.send(message);
//            return true;
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }


    public List<Comment> getCommentsForIdea(int ideaId) {

        return commentRepository.getAllByIdeaIdOrderByDateDesc(ideaId);
    }
}
