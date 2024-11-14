package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Member;
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
                    return true;
                case "findAPI":
                    if (object instanceof Book) {
                        Book book = (Book) object;
                        this.bookResult = BookInfoFetcher.searchBookByISBN(String.valueOf(book.getISBN()));
                    }
                    return true;
                default:
                    return false;
            }
        } catch (SQLException e) {
            CustomerAlter.showAlter(e.getMessage());
            return false; // Thất bại
        }
    }
}
