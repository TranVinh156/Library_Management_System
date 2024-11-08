package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Member;

import java.sql.SQLException;

public class AdminCommand implements Command {
    private String action;
    private Object object;
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();

    public AdminCommand(String action, Object object) {
        this.action = action;
        this.object = object;
    }

    public boolean execute() {
        try {
            System.out.println(action + "neeeeee");
            switch (action) {
                case "add":
                    if (object instanceof Book) {
                        bookDAO.add((Book) object);
                    } else if (object instanceof Member) {
                        memberDAO.add((Member) object);
                    }
                    return true; // Thành công
                case "delete":
                    // Thêm logic cho delete nếu cần
                    return true;
                case "edit":
                    if (object instanceof Book) {
                        bookDAO.update((Book) object);
                    }
                    if (object instanceof Member) {
                        System.out.println("thememmmem");
                        memberDAO.update((Member) object);
                    }
                    return true;
                default:
                    return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            CustomerAlter.showMessage(e.getMessage());
            return false; // Thất bại
        }
    }
}
