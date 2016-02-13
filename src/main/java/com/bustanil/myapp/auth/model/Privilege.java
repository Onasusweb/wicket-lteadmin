package com.bustanil.myapp.auth.model;

import java.io.Serializable;

public class Privilege implements Serializable {

    private Long id;
    private String name;
    private String path;

    public Privilege(Long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    private PrivilegeGroup group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrivilegeGroup getGroup() {
        return group;
    }

    public void setGroup(PrivilegeGroup group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
