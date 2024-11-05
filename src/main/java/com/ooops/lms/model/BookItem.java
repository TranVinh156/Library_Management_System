package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookItemStatus;

import java.util.List;

public class BookItem extends Book {
    private int barcode;
    private BookItemStatus status;
    private String note = "";

    public BookItem() {

    }

    // Sử dụng khi add
    public BookItem(int ISBN, BookItemStatus status, String note) {
        super.setISBN(ISBN);
        this.status = status;
        this.note = note;
    }

    // sử dụng khi find
    public BookItem(int barcode, BookItemStatus status, String note, Book book) {
        super(book);
        this.barcode = barcode;
        this.status = status;
        this.note = note;
    }

    public int getBarcode() {
        return barcode;
    }

    public BookItemStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setStatus(BookItemStatus status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
