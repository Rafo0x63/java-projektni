package com.prod.hydraulicsystemsmaintenance.entities;

public class CurrentUser extends User {

    public CurrentUser(User user) {
        super(user.getId(), user.getName(), user.getUsername(), user.getAdministrator());
    }


}
