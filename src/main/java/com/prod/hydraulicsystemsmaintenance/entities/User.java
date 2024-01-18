package com.prod.hydraulicsystemsmaintenance.entities;

import com.prod.hydraulicsystemsmaintenance.database.Database;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String passwordHash;
    private Integer administrator;
    private String isAdmin;

    public User(int id, String name, String username, String password, Integer administrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.passwordHash = Database.hashPassword(password);
        this.administrator = administrator;
        this.isAdmin = administrator == 1 ? "Yes" : "No";
    }

    public User(String name, String username, String password, Integer administrator) {
        this.name = name;
        this.username = username;
        this.passwordHash = password;
        this.administrator = administrator;
        this.isAdmin = administrator == 1 ? "Yes" : "No";
    }

    public User(Integer id, String name, String username, Integer administrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.administrator = administrator;
        this.isAdmin = administrator == 1 ? "Yes" : "No";
    }

    public User(String username) {
        this.username = username;
    }

    public User() {

    }

    public User(String name, String username, Integer administrator) {
        this.name = name;
        this.username = username;
        this.administrator = administrator;
        this.isAdmin = administrator == 1 ? "Yes" : "No";
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return passwordHash;
    }

    public Integer getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }

    public String getIsAdmin() {
        return this.isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + passwordHash + '\'' +
                ", administrator=" + administrator +
                '}';
    }
}
