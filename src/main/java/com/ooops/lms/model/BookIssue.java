package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookIssueStatus;

public class BookIssue {
    private Member member;
    private BookItem bookItem;
    private BookIssueStatus status;

    public BookIssue(Member member, BookItem bookItem) {
        this.member = member;
        this.bookItem = bookItem;
    }


    public void setMember(Member member) {
        this.member = member;
    }

    public void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
    }

    public Member getMember() {
        return member;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public BookIssueStatus getStatus() {
        return status;
    }

    public void setStatus(BookIssueStatus status) {
        this.status = status;
    }
}
