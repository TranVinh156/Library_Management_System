package com.ooops.lms.model;

import com.ooops.lms.controller.BasicController;
import com.ooops.lms.model.datatype.Person;

public class Admin extends Account {
    public Admin(String username, String password, Person person) {
        super(username, password, person);
    }

    public static boolean addBook(Book book) {
        return false;
    }

    public static boolean deleteBook(Book book) {
        return false;
    }

    public static boolean updateBook(Book book) {
        return false;
    }

    public static boolean blockMember(Member member) {
        return false;
    }

    public static boolean unblockMember(Member member) {
        return false;
    }

    public static boolean addMember(Member member) {
    return false;
    }

    public static boolean addIssueBook(Book book, Member member) {
        return false;
    }

    public static boolean updateIssueBook(Book book, Member member) {
        return false;
    }

    public static boolean deleteIssueBook(Book book, Member member) {
        return false;
    }
}
