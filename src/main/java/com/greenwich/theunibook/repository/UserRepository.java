package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM user u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);





}
