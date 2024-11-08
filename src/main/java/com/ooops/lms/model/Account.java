package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;

public abstract class Account {
    protected int accountId;
    protected String username;
    protected String password;
    protected Person person;
    protected AccountStatus status;
    protected String createdDate;

    public Account(Person person) {
        this.person = person;
    }

    public Account(String username, String password, Person person) {
        this.username = username;
        this.password = password;
        this.person = person;
    }

    public Account(int acoountId, String username, String password, AccountStatus status, String createdDate, Person person) {
        this.accountId = acoountId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.createdDate = createdDate;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
