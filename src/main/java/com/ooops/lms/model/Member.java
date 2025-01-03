package com.ooops.lms.model;

import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;

public class Member extends Account {
    private int totalBooksCheckOut;
    private int totalBooksLost;

    public Member(String username, String password, Person person) {
        super(username, password, person);
    }

    public Member(int accountId, String username, String password, AccountStatus status, String createdDate, Person person) {
        super(accountId, username, password, status, createdDate, person);
    }

    public Member(Person person) {
        super(person);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member) {
            Member member = (Member) obj;
            return getPerson().getId() == member.getPerson().getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getPerson().getId());
    }

    @Override
    public String toString() {
        return getPerson().getId() + "";
    }

    public int getTotalBooksCheckOut() {
        return totalBooksCheckOut;
    }

    public void setTotalBooksCheckOut(int totalBooksCheckOut) {
        this.totalBooksCheckOut = totalBooksCheckOut;
    }

    public int getTotalBooksLost() {
        return totalBooksLost;
    }
    public void setTotalBooksLost(int totalBooksLost) {
        this.totalBooksLost = totalBooksLost;
    }
}
