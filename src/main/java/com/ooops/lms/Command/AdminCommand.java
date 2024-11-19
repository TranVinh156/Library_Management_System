package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.barcode.BarcodeScanner;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.*;
import com.ooops.lms.model.*;
import com.ooops.lms.model.enums.AccountStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminCommand implements Command {
    private String action;
    private Object object;
    private Member memberResult;
    private Book bookResult;
    private BookDAO bookDAO;
    private BookItem bookItemResult;
    private BarcodeScanner barcodeScanner = new BarcodeScanner();

    public AdminCommand(String action, Object object) {
        this.action = action;
        this.object = object;
        bookDAO = BookDAO.getInstance();
    }

    public Member getMemberResult() {
        return memberResult;
    }

    public Book getBookResult() {
        return bookResult;
    }

    public BookItem getBookItemResult() {
        return bookItemResult;
    }

    /**
     * các command người dùng muốn thực thi.
     * Hiện tại có các lệnh add,delete,edit.
     *
     * @return true - thành công, false - thất bại
     */
    public boolean execute() {
        try {
            switch (action) {
                case "add":
                    if (object instanceof Book) {
                        BookDAO.getInstance().add((Book) object);
                    } else if (object instanceof Member) {
                        MemberDAO.getInstance().add((Member) object);
                    } else if (object instanceof BookReservation) {
                        System.out.println("Dang them borrow:" + ((BookReservation)object).getBookItem().getBarcode());
                        BookReservationDAO.getInstance().add((BookReservation) object);
                    } else if (object instanceof BookIssue) {
                        BookIssueDAO.getInstance().add((BookIssue) object);
                    }
                    return true;
                case "delete":
                    if (object instanceof Book) {
                        BookDAO.getInstance().delete((Book) object);
                    }
                    if (object instanceof Member) {
                        MemberDAO.getInstance().delete((Member) object);
                    }
                    return true;
                case "edit":
                    if (object instanceof Book) {
                        Book book = (Book) object;
                        bookDAO.update((Book) object);
                    }
                    if (object instanceof Member) {
                        MemberDAO.getInstance().update((Member) object);
                    }
                    return true;
                case "block":
                    if (object instanceof Member) {
                        Member member = (Member) object;
                        member.setStatus(AccountStatus.BLOCKED);
                    }
                    return true;
                case "unblock":
                    if (object instanceof Member) {
                        Member member = (Member) object;
                        member.setStatus(AccountStatus.ACTIVE);
                    }
                    return true;
                case "find":
                    if (object instanceof Member) {
                        Member member = (Member) object;
                        this.memberResult = MemberDAO.getInstance().find(member.getAccountId());
                    }
                    if(object instanceof BookItem) {
                        BookItem bookItem = (BookItem) object;
                        this.bookItemResult = BookItemDAO.getInstance().find(bookItem.getBarcode());
                    }
                    return true;
                case "scan":
                    if(object instanceof BookItem) {
                        bookItemResult = BookItemDAO.getInstance().find(Integer.valueOf(barcodeScanner.scanBarcodeFromCamera()));
                    } else if(object instanceof Book) {
                        bookResult = BookDAO.getInstance().find(Integer.valueOf(barcodeScanner.scanBarcodeFromCamera()));
                    } else if(object instanceof Member) {
                        memberResult = MemberDAO.getInstance().find(Long.valueOf(barcodeScanner.scanBarcodeFromCamera()));
                    }
                    return true;
                default:
                    return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi AdminCommand:" + e.getMessage());
            return false; // Thất bại
        }
    }
}
