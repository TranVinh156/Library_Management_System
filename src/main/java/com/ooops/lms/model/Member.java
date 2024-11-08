package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;

public class Member extends Account {
    private int totalBooksCheckOut;

    public Member(String username, String password, Person person) {
        super(username, password, person);
    }

    public Member(int accountId, String username, String password, AccountStatus status, String createdDate, Person person) {
        super(accountId, username, password, status, createdDate, person);
    }

    public Member(Person person) {
        super(person);
    }

    public int getTotalBooksCheckOut() {
        return totalBooksCheckOut;
    }
}
