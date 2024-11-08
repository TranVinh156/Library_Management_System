package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookIssueStatus;

public class BookIssue {
    private int issueID;
    private Member member;
    private BookItem bookItem;
    private String createdDate;
    private String dueDate;
    private String returnDate;
    private BookIssueStatus status;

    public BookIssue(int issueID, Member member, BookItem bookItem
            , String createdDate, String dueDate, String returnDate, BookIssueStatus status) {
        this.issueID = issueID;
        this.member = member;
        this.bookItem = bookItem;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public BookIssue(Member member, BookItem bookItem, String createdDate, String dueDate) {
        this.member = member;
        this.bookItem = bookItem;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
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

    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
