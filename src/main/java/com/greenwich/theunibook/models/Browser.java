package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("browsers")
public class Browser {
    @Id
    private int id;
    @Column("browser_name")
    private String name;
    private int times_used;

    public Browser(String name, int times_used) {
        this.name = name;
        this.times_used = times_used;
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

    public int getTimes_used() {
        return times_used;
    }

    public void setTimes_used(int times_used) {
        this.times_used = times_used;
    }
}
