package com.bustanil.myapp.auth.dto;

import com.bustanil.myapp.auth.model.PrivilegeGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeGroupDTO implements Serializable {

    private Long id;
    private String name;
    private List<PrivilegeDTO> privileges = new ArrayList<>();

    public PrivilegeGroupDTO() {
    }

    public PrivilegeGroupDTO(PrivilegeGroup group) {
        this.id = group.getId();
        this.name = group.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
