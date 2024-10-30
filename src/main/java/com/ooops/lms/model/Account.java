package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;

public abstract class Account {
    private String username;
    private String password;
    private Person person;

    public Account(String username, String password, Person person) {
        this.username = username;
        this.password = password;
        this.person = person;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Person getPerson() {
        return person;
    }

    public boolean resetPassword() {
        return false;
    }

    public boolean changePassword(String password) {
        return false;
    }
}
