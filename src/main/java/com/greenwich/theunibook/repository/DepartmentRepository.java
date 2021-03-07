package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Department;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {

    @Query("SELECT department_name FROM department WHERE id_department = :departmentId")
    String getDepartmentNameById(int departmentId);

//    @Query("SELECT id_department FROM department")
//    List<Integer> getDepartmentIds();


    @Query("SELECT * FROM department")
    List<Department> getAll();
}
