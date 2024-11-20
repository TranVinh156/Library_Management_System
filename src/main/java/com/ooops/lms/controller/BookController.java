package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.*;
import com.ooops.lms.model.*;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookController {
    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String BOOKMARK_FXML = "/com/ooops/lms/library_management_system/Bookmark-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String COMMENT_FXML = "/com/ooops/lms/library_management_system/Comment-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private Book book;

    @FXML
    VBox bookBox;
    @FXML
    private ChoiceBox<String> starChoiceBox;
    @FXML
    private Label authorNameLabel;
    @FXML
    private ImageView bookImage, starImage;
    @FXML
    private Label bookNameLabel;
    @FXML
    private Text contentText;
    @FXML
    private VBox commentsVBox;

    public void initialize() {
        starChoiceBox.getItems().addAll("tất cả", "5 sao", "4 sao", "3 sao", "2 sao", "1 sao");
        starChoiceBox.setValue("tất cả");
    }

    public void setData() {
        bookImage.setImage(new Image(book.getImagePath()));

        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for (int i = 0; i < authorList.size(); i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        contentText.setText(book.getDescription());

        //comment
        List<Comment> comments = new ArrayList<>();
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("ISBN", book.getISBN());
            comments = CommentDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < comments.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(COMMENT_FXML));
                VBox cardBox = fxmlLoader.load();
                CommentController cardController = fxmlLoader.getController();
                cardController.setData(comments.get(i));
                commentsVBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            if (bookBox.getChildren().contains(content)) {
                bookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onReserveBookButtonAction(ActionEvent actionEvent) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn đặt trước sách chứ gì?");
        if (confirmYes) {
            try {
                HistoryController historyController =
                        FXMLLoaderUtil.getInstance().getController(HISTORY_FXML);
                if (historyController != null) {
                    historyController.addReservedBook(this.book);
                }
                Map<String, Object> criteria = new HashMap<>();
                criteria.put("ISBN", book.getISBN());
                List<BookItem> bookItem = BookItemDAO.getInstance().searchByCriteria(criteria);
                BookReservation bookReservation = new BookReservation(UserMenuController.getMember()
                        , bookItem.get(0),LocalDate.now().toString()
                        , LocalDate.now().plusDays(3).toString());
                try {
                    BookReservationDAO.getInstance().add(bookReservation);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                CustomerAlter.showMessage("Đã ghi nhận, vui lòng n mượn trong 3 ngày");
                BookManager.getInstance().addReservedBook(bookReservation);
                SettingController settingController = FXMLLoaderUtil.getInstance().getController(SETTING_FXML);
                settingController.updateReservedBookSize();
            } catch (RuntimeException | IOException | SQLException e) {
                e.printStackTrace();
                CustomerAlter.showMessage("Lỗi :v");
            }
        }

    }

    public void onBookmarkButtonAction(ActionEvent actionEvent) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn đánh dấu sách này à?");
        if (confirmYes) {
            try {
                BookmarkController bookmarkController =
                        FXMLLoaderUtil.getInstance().getController(BOOKMARK_FXML);
                if (bookmarkController != null) {
                    bookmarkController.addBookmark(this.book);
                }
                Map<String, Object> criteria = new HashMap<>();
                criteria.put("ISBN", book.getISBN());
                List<BookItem> bookItem = BookItemDAO.getInstance().searchByCriteria(criteria);
                BookMark bookMark = new BookMark(UserMenuController.getMember()
                        , bookItem.get(0));
                try {
                    BookMarkDAO.getInstance().add(bookMark);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                CustomerAlter.showMessage("Đã thêm vô đánh dấu sách nè");
            } catch (RuntimeException | IOException | SQLException e) {
                e.printStackTrace();
                CustomerAlter.showMessage("Đánh dấu rồi còn đánh dấu nữa t đánh m á :)");
            }
        }
    }

    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        setData();
    }
}
