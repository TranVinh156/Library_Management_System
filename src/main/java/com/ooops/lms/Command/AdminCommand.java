package com.ooops.lms.Command;

import com.google.api.services.books.v1.model.Volumeseriesinfo;
import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.barcode.BarcodeScanner;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.*;
import com.ooops.lms.model.*;
import com.ooops.lms.model.enums.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
                        return true;
                    } else if (object instanceof Member) {
                        MemberDAO.getInstance().add((Member) object);
                        return true;
                    } else if (object instanceof BookReservation) {
                        BookReservationDAO.getInstance().add((BookReservation) object);
                        return true;
                    } else if (object instanceof BookIssue) {
                        Map<String, Object> findCriteria = new HashMap<>();
                        findCriteria.put("BookReservationStatus", BookReservationStatus.WAITING);
                        findCriteria.put("member_ID", ((BookIssue) object).getMember().getPerson().getId());
                        findCriteria.put("barcode", ((BookIssue) object).getBookItem().getBarcode());
                        List<BookReservation> bookReservationsList = BookReservationDAO.getInstance().searchByCriteria(findCriteria);
                        if (bookReservationsList.size() > 0) {
                            BookIssue newBookIssue = new BookIssue(((BookIssue) object).getMember(),bookReservationsList.getFirst().getBookItem(),((BookIssue) object).getCreatedDate(),((BookIssue) object).getDueDate());
                            BookIssueDAO.getInstance().add(newBookIssue);

                            bookReservationsList.getFirst().setStatus(BookReservationStatus.COMPLETED);
                            BookReservationDAO.getInstance().update(bookReservationsList.getFirst());
                            return true;
                        } else {
                            BookIssueDAO.getInstance().add((BookIssue) object);
                            return true;
                        }
                    }
                    return false;
                case "delete":
                    if (object instanceof Book) {
                        BookDAO.getInstance().delete((Book) object);
                        return true;
                    } else
                    if (object instanceof Member) {
                        MemberDAO.getInstance().delete((Member) object);
                        return true;
                    } else if(object instanceof BookReservation) {
                        BookItem bookItem = ((BookReservation) object).getBookItem();
                        bookItem.setStatus(BookItemStatus.AVAILABLE);
                        BookItemDAO.getInstance().update(bookItem);
                        BookReservation bookReservation = (BookReservation) object;
                        bookReservation.setStatus(BookReservationStatus.CANCELED);
                        BookReservationDAO.getInstance().update(bookReservation);
                        BookReservationDAO.getInstance().delete((BookReservation) object);
                        return true;
                    } else if(object instanceof BookIssue) {
                        BookItem bookItem = ((BookIssue) object).getBookItem();
                        bookItem.setStatus(BookItemStatus.AVAILABLE);
                        BookItemDAO.getInstance().update(bookItem);
                        BookIssueDAO.getInstance().delete((BookIssue) object);
                        return true;
                    }
                    return false;
                case "edit":
                    if (object instanceof Book) {
                        Book book = (Book) object;
                        bookDAO.update((Book) object);
                        return true;
                    } else if (object instanceof Member) {
                        MemberDAO.getInstance().update((Member) object);
                        return true;
                    } else if (object instanceof Report) {
                        ReportDAO.getInstance().update((Report) object);
                        return true;
                    } else if(object instanceof BookIssue) {
                        BookIssueDAO.getInstance().update((BookIssue) object);
                        return true;
                    } else if(object instanceof BookReservation) {
                        BookReservationDAO.getInstance().update((BookReservation) object);
                        return true;
                    }
                    return false;
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
                    if (object instanceof BookItem) {
                        BookItem bookItem = (BookItem) object;
                        this.bookItemResult = BookItemDAO.getInstance().find(bookItem.getBarcode());
                    }
                    return true;
                case "scan":
                    if (object instanceof BookItem) {
                        bookItemResult = BookItemDAO.getInstance().find(Long.valueOf(barcodeScanner.scanBarcodeFromCamera()));
                    } else if (object instanceof Book) {
                        String barcode = barcodeScanner.scanBarcodeFromCamera();
                        bookResult = BookInfoFetcher.searchBookByISBN(barcode);
                        if (bookResult == null) {
                            return false;
                        }
                    } else if (object instanceof Member) {
                        memberResult = MemberDAO.getInstance().find(Long.valueOf(barcodeScanner.scanBarcodeFromCamera()));
                    }
                    return true;
                default:
                    return false;
            }
        } catch (
                SQLException e) {
            System.out.println("Lỗi AdminCommand:" + e.getMessage());
            CustomerAlter.showAlter(e.getMessage());
            return false; // Thất bại
        }
    }
}
