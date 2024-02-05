package com.prod.hydraulicsystemsmaintenance.entities;

import com.prod.hydraulicsystemsmaintenance.database.Database;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String passwordHash;
    private Integer administrator;
    private String isAdmin;

    public User(Integer  id, String name, String username, String password, Integer administrator) {
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
        if (administrator != null) {
            this.isAdmin = administrator == 1 ? "Yes" : "No";
        }
    }

    public User(Integer id, String name, String username, Integer administrator) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.administrator = administrator;
        this.isAdmin = administrator == 1 ? "Yes" : "No";
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String username;
        private String passwordHash;
        private Integer administrator;
        private String isAdmin;
        public Builder(String username) {
            this.username = username;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder administrator(Integer administrator) {
            this.administrator = administrator;
            this.isAdmin = administrator == 0 ? "No" : "Yes";
            return this;
        }

        public User build() {
            return new User(this.name, this.username, this.passwordHash, this.administrator);
        }


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

    public String toChangeString() {
        String admin = administrator == 1 ? "Admin" : "Technician";
        return STR."\{username} (\{admin})";
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id + "\n" +
                ", name='" + name + "\'\n" +
                ", username='" + username + "\'\n" +
                ", administrator=" + administrator +
                '}';
    }
}
