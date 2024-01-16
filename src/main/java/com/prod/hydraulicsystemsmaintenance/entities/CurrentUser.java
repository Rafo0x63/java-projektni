package com.prod.hydraulicsystemsmaintenance.entities;

public class CurrentUser {
    private User user = null;

    public CurrentUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
