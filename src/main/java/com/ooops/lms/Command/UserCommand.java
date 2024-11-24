package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.*;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Comment;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.enums.AccountStatus;
import com.ooops.lms.util.BookManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserCommand {
    private String action;
    private Object object;
    private Member memberResult;

    private List<Book> bookList = new ArrayList<>();

    public UserCommand(String action, Object object) {
        this.action = action;
        this.object = object;
    }

    public Member getMemberResult() {
        return memberResult;
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
                    } else if (object instanceof Comment) {
                        CommentDAO.getInstance().add((Comment) object);
                    } else if (object instanceof BookItem) {
                        BookItemDAO.getInstance().add((BookItem) object);
                    }
                    return true;
                case "getPopularBooks":
                    this.object = BookManager.getInstance().getPopularBooks();
                    return true;
                case "getHighRankBooks":
                    this.object = BookManager.getInstance().getHighRankBooks();
                    return true;
                case "getAllBooks":
                    this.object = BookManager.getInstance().getAllBooks();
                    return true;
                case "searchBookByCategory":
                    Map<String, Object> criteria = (Map<String, Object>) object;
                    this.object = BookDAO.getInstance().searchByCriteria(criteria);
                    return true;
                default:
                    return false;
            }
        } catch (SQLException e) {
            CustomerAlter.showAlter(e.getMessage());
            return false; // Thất bại
        }
    }

    public Object getObject() {
        return this.object;
    }
}
