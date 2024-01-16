package com.prod.hydraulicsystemsmaintenance.entities;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Integer administrator;

    public User(int id, String name, String username, String password, Integer administrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.administrator = administrator;
    }

    public User(String name, String username, String password, Integer administrator) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.administrator = administrator;
    }

    public User(Integer id, String name, String username, Integer administrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.administrator = administrator;
    }

    public User(String username) {
        this.username = username;
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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", administrator=" + administrator +
                '}';
    }
}
