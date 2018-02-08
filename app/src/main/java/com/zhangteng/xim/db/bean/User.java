package com.zhangteng.xim.db.bean;

/**
 * Created by swing on 2017/9/5.
 */
public class User {
    private Long id;
    private String name;
    private int tempUsageCount; // not persisted

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

