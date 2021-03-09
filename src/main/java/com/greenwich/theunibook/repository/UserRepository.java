package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.User;
import org.springframework.data.jdbc.repository.query.Modifying;
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

    @Query("SELECT users.email FROM users INNER JOIN department ON users.id_users = department.id_QA_coordinator\n" +
            "where department.id_department = :departmentId")
    String getQACoordinatorEmail(int departmentId);

    @Query(value = "SELECT user_fname FROM users where id_users = :QACoordinatorId")
    String getQACoordinatorName(int QACoordinatorId);

    @Query("SELECT id_QA_coordinator FROM department WHERE id_department = :departmentId")
    int getQACoordinatorId(int departmentId);

    @Modifying
    @Query("UPDATE users SET password = :password WHERE email = :email")
    void changePasswordWithEmail(String password, String email);

    @Modifying
    @Query("UPDATE users SET password = :password WHERE id_users = :userId")
    void changePassword(String password, int userId);

    @Query("SELECT * FROM users WHERE id_department = :departmentId")
    List<User> getAllUsersInDepartment(int departmentId);

    @Query("SELECT DISTINCT(id_users) FROM ideas WHERE department_id = :departmentId")
    List<Integer> getAllUserIdsInIdeas(int departmentId);

    @Query("SELECT * FROM users WHERE id_users = :userId")
    User getUserById(int userId);

//    @Query("SELECT COUNT ")
//    int
}
