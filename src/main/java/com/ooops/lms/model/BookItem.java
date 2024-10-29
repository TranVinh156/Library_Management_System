package com.ooops.lms.model;

import com.ooops.lms.model.enums.BookItemStatus;

import java.util.List;

public class BookItem extends Book {
    private int barcode;
    private int ISBN;
    private BookItemStatus status;
    private String note = "";

    public BookItem() {
        super();
    }

    public BookItem(int barcode, int ISBN, BookItemStatus status, String note) {
        super();
        this.barcode = barcode;
        this.ISBN = ISBN;
        this.status = status;
        this.note = note;
    }

    public int getBarcode() {
        return barcode;
    }

    @Override
    public int getISBN() {
        return ISBN;
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
