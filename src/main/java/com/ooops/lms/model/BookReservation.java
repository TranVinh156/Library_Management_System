package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookReservationStatus;

public class BookReservation {
    private int reservationId;
    private Member member;
    private BookItem bookItem;
    private String createdDate;
    private String dueDate;
    private BookReservationStatus status;

    public BookReservation(Member member, BookItem bookItem, String createdDate, String dueDate) {
        this.member = member;
        this.bookItem = bookItem;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public BookReservation(int reservationId, Member member, BookItem bookItem
            , String createdDate, String dueDate, BookReservationStatus status) {
        this.reservationId = reservationId;
        this.member = member;
        this.bookItem = bookItem;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
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

    public BookReservationStatus getStatus() {
        return status;
    }

    public void setStatus(BookReservationStatus status) {
        this.status = status;
    }
}
