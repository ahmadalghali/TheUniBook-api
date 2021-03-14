package com.greenwich.theunibook.models;

import org.springframework.data.annotation.Id;

public class Browser {
    @Id
    private int id;
    private String name;
    private int useCount;


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

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

}
