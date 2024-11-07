package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookReservationStatus;

public class BookReservation {
    private Member member;
    private BookItem bookItem;
    private BookReservationStatus status;

    public BookReservation(Member member, BookItem bookItem) {
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

    public void setStatus(BookReservationStatus status) {
        this.status = status;
    }

    public BookReservationStatus getStatus() {
        return status;
    }
}
