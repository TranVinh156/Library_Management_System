package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;

public abstract class Account {
    private String username;
    private String password;
    private Person person;
    private AccountStatus status;

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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean resetPassword() {
        return false;
    }

    public boolean changePassword(String password) {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
