package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

//    List<Comment> getAllByIdeaIdOrderByDateDesc(int ideaId);


    @Query("SELECT c.* FROM comments c  \n" +
            "JOIN users u ON u.id_users = c.id_users\n" +
            "WHERE id_ideas = :ideaId AND  u.is_hidden = 0  \n" +
            "ORDER BY c.date DESC")
    List<Comment> getAllByIdeaIdOrderByDateDesc(int ideaId);


    @Query("select id_users from ideas where id_ideas = :ideaId")
    int getIdeaAuthorId(int ideaId);

    @Query("SELECT users.email\n" +
            "FROM users\n" +
            "INNER JOIN ideas ON users.id_users=ideas.id_users where id_ideas = :ideaId")
    String getIdeaAuthorEmail(int ideaId);

    @Query("SELECT COUNT(id_comment) FROM comments")
    int numOfComments();


    int countByIdeaId(int ideaId);
    @Query("SELECT users.user_fname FROM users WHERE users.id_users = :ideaAuthorId")
    String getAuthorName(int ideaAuthorId);
}
