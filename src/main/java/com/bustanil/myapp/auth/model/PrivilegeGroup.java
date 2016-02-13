package com.bustanil.myapp.auth.model;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeGroup {

    private Long id;
    private String name;

    public PrivilegeGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private List<Privilege> privileges = new ArrayList<>();

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

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(Privilege privilege) {
        privilege.setGroup(this);
        getPrivileges().add(privilege);
    }
}
