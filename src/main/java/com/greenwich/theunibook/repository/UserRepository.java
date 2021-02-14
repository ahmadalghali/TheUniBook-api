package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM users  WHERE email = :email")
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users  WHERE username = :username")
    User findByUsername(@Param("username") String username);

}
