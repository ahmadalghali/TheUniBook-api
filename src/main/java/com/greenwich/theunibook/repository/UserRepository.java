package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM users  WHERE email = :email")
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users")
    List<User> getAllUsers();

    @Query(value = "select id_users from ideas where id_ideas = :ideaId")
    int getIdeaAuthorId(int ideaId);

    @Query("SELECT users.email\n" +
            "FROM users\n" +
            "INNER JOIN ideas ON users.id_users=ideas.id_users where id_ideas = :ideaId")
    String getIdeaAuthorEmail(int ideaId);
}
