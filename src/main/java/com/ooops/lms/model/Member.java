package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;

public class Member extends Account {
    private int totalBooksCheckOut;

    public Member(String username, String password, Person person) {
        super(username, password, person);
    }

    public int getTotalBooksCheckOut() {
        return totalBooksCheckOut;
    }
}
