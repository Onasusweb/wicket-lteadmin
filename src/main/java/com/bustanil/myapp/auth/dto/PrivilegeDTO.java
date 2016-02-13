package com.bustanil.myapp.auth.dto;

import com.bustanil.myapp.auth.model.Privilege;

import java.io.Serializable;

public class PrivilegeDTO implements Serializable {

    private Long id;
    private String name;
    private String path;

    public PrivilegeDTO(Privilege privilege) {
        this.id = privilege.getId();
        this.name = privilege.getName();
        this.path = privilege.getPath();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
