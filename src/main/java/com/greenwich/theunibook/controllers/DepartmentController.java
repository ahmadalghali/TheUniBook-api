package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Department;
import com.greenwich.theunibook.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DepartmentController {


    @Autowired
    DepartmentRepository departmentRepository;

    @GetMapping("/departments")
    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        departmentRepository.findAll().forEach(departments::add);
        return departments;
    }


}
