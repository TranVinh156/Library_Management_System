package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.util.BookManager;
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


    /**
     * về Setting.
     * @param actionEvent khi ấn
     */
    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            bookReservationList = BookManager.getInstance().getReservedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            bookBorrowedList = BookManager.getInstance().getBorrowedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            bookBorrowingList = BookManager.getInstance().getBorrowingBooks();
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
                cardController.setReservedBook(this,bookReservationList.get(i).getBookItem());
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

    /**
     * sang đánh giá
     * @param actionEvent khi ấn
     */
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

    /**
     * thêm sách đặt trước vào vị trí.
     * @param bookReservation sách đặt trước
     * @throws IOException ném ngoại lệ
     */
    public void addReservedBook(BookReservation bookReservation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
        VBox cardBox = fxmlLoader.load();
        BookCard2Controller cardController = fxmlLoader.getController();
        cardController.setData(bookReservation.getBookItem());
        bookReservationList.add(bookReservation);
        cardController.setReservedBook(this,bookReservation.getBookItem());
        System.out.println("add and size =" + bookReservationList.size());
        reservedHBox.getChildren().add(cardBox);
        SettingController settingController = FXMLLoaderUtil.getInstance().getController(SETTING_FXML);
        settingController.updateReservedBookSize();
    }

    /**
     * xoá sách đặt trước.
     * @param bookItem sách
     * @param vBox hộp chứa sách
     * @throws IOException ngoại lệ
     */
    public void deleteBookReserved(BookItem bookItem,VBox vBox) throws IOException {
        if(!CustomerAlter.showAlter("Bạn huỷ đặt trước sách này?")) {
            return;
        }
        Map<String,Object> criteria = new HashMap<>();
        criteria.put("barcode",bookItem.getBarcode());
        int index = findBookReserved(bookItem.getBarcode());

        try {
            List<BookReservation> bookReservations = BookReservationDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(index!=-1) {
            try {
                List<BookReservation> bookReservations = BookReservationDAO.getInstance().searchByCriteria(criteria);
                if(bookReservations.size()>0) {
                    bookItem.setStatus(BookItemStatus.AVAILABLE);
                    BookItemDAO.getInstance().update(bookItem);
                    BookReservation bookReservation = BookReservationDAO.getInstance().find(bookReservations.getFirst().getReservationId());
                    BookReservationDAO.getInstance().delete(bookReservation);
                    bookReservationList.remove(index);
                    SettingController settingController = FXMLLoaderUtil.getInstance().getController(SETTING_FXML);
                    settingController.updateReservedBookSize();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        reservedHBox.getChildren().remove(vBox);
    }

    /**
     * tìm sách theo barCode
     * @param barCode barCode
     * @return sách
     */
    private int findBookReserved(long barCode) {
        try {
            bookReservationList = BookManager.getInstance().getReservedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0;i<bookReservationList.size();i++) {
            if (bookReservationList.get(i).getBookItem().getBarcode() == barCode) {
                return i;
            }
        }
        return -1;
    }
}
