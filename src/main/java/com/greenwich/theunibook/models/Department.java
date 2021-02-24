package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Department {

    @Id
    @Column("id_department")
    private int id;
    @Column("department_name")
    private String name;

    public Department(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
