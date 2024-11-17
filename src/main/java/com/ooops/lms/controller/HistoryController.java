package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class HistoryController implements Initializable {
    @FXML
    private VBox contentBox;
    @FXML
    private HBox borrowingHBox;
    @FXML
    private HBox borrowedHBox;
    @FXML
    private HBox reservedHBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String RATINGBOOK_FXML = "/com/ooops/lms/library_management_system/RatingBook-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private List<BookReservation> bookReservationList = new ArrayList<>();
    private List<BookIssue> bookBorrowingList = new ArrayList<>();
    private List<BookIssue> bookBorrowedList = new ArrayList<>();


    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("member_ID", UserMenuController.member.getPerson().getId());
        try {
            bookReservationList = BookReservationDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        criteria.put("status", BookIssueStatus.RETURNED);

        try {
            bookBorrowedList = BookIssueDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> criteria2 = new HashMap<>();
        criteria.put("member_ID", UserMenuController.member.getPerson().getId());
        criteria.put("status", BookIssueStatus.BORROWED);
        try {
            bookBorrowingList = BookIssueDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < bookBorrowingList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookBorrowingList.get(i).getBookItem());
                borrowingHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bookReservationList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookReservationList.get(i).getBookItem());
                reservedHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bookBorrowedList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookBorrowedList.get(i).getBookItem());
                borrowedHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRatingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(RATINGBOOK_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            try {
                RatingBookController ratingBookController = fxmlLoaderUtil.getController(RATINGBOOK_FXML);
                ratingBookController.setData(bookBorrowedList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean addReservedBook(Book book) throws IOException {
        if(bookReservationList.contains(book)) return false;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
        VBox cardBox = fxmlLoader.load();
        BookCard2Controller cardController = fxmlLoader.getController();
        cardController.setData(book);
        borrowedHBox.getChildren().add(cardBox);
        return true;
    }
}
