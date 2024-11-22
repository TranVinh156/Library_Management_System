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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookItem) {
            BookItem bookItem = (BookItem) obj;
            return bookItem.getBarcode() == this.barcode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.barcode);
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public BookItemStatus getStatus() {
        return this.status;
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
