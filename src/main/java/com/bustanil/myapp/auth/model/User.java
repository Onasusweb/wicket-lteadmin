package com.bustanil.myapp.auth.model;

import java.io.Serializable;

public class User implements Serializable {

    private String kode;
    private String name;
    private String password;
    private Role role;

    public User(String kode, String name, String password, Role role) {
        this.kode = kode;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
