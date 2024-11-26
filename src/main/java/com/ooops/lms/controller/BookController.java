package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.animation.Animation;
import com.ooops.lms.database.dao.*;
import com.ooops.lms.model.*;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ooops.lms.controller.BookSuggestionCardController.executor;

public class BookController {
    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String BOOKMARK_FXML = "/com/ooops/lms/library_management_system/Bookmark-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String COMMENT_FXML = "/com/ooops/lms/library_management_system/Comment-view.fxml";

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
    @FXML
    HBox categoryHbox;
    @FXML
    Button bookmarkButton;

    public void initialize() {
        starChoiceBox.getItems().addAll("tất cả", "5 sao", "4 sao", "3 sao", "2 sao", "1 sao");
        starChoiceBox.setValue("tất cả");

        starChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue != null && !oldValue.equals(newValue)) {
                    showComment(fromRateToInt(starChoiceBox.getValue()));
                }
            }
        });
        ThemeManager.getInstance().addPane(bookBox);
    }

    public void setData() {
        Task<Image> loadImageTask = BookManager.getInstance().createLoadImageTask(book);


        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);

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
        categoryHbox.getChildren().clear();
        List<Category> categories = book.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            Label label = new Label(categories.get(i).toString());
            label.getStyleClass().add("label-4");
            categoryHbox.getChildren().add(label);
        }
        //mark
        if (BookManager.getInstance().isContainedInMarkedBookList(book.getISBN())) {
            bookmarkButton.setText("Huỷ Đánh Dấu");
        }
    }

    private void showComment(int star) {
        commentsVBox.getChildren().clear();
        List<Comment> comments = new ArrayList<>();
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("ISBN", book.getISBN());
            comments = CommentDAO.getInstance().searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < comments.size(); i++) {
            if (star == 0 || comments.get(i).getRate() == star) {
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
    }

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(DASHBOARD_FXML);
        if (content != null) {
            if (bookBox.getChildren().contains(content)) {
                bookBox.getChildren().remove(content);
            }
            FXMLLoaderUtil.getInstance().updateContentBox(content);
            FXMLLoaderUtil.getInstance().changeColorWhenBack();
        }
    }

    public void onReserveBookButtonAction(ActionEvent actionEvent) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn đặt trước sách chứ gì?");
        if (confirmYes) {
            try {
                Map<String, Object> criteria = new HashMap<>();
                criteria.put("ISBN", book.getISBN());
                criteria.put("BookItemStatus", "AVAILABLE");
                List<BookItem> bookItems = BookItemDAO.getInstance().searchByCriteria(criteria);
                if (bookItems.size() > 0) {
                    BookItem bookItem = BookItemDAO.getInstance().find(bookItems.get(0).getBarcode());
                    bookItem.setStatus(BookItemStatus.RESERVED);
                    BookItemDAO.getInstance().update(bookItem);
                    BookReservation bookReservation = new BookReservation(UserMenuController.getMember()
                            , bookItem, LocalDate.now().toString()
                            , LocalDate.now().plusDays(3).toString());
                    try {
                        BookReservationDAO.getInstance().add(bookReservation);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    HistoryController historyController =
                            FXMLLoaderUtil.getInstance().getController(HISTORY_FXML);
                    if (historyController != null) {
                        historyController.addReservedBook(bookReservation);
                    }
                    Animation.getInstance().showMessage("Đã ghi nhận, vui lòng mượn trong 3 ngày");

                } else {
                    CustomerAlter.showMessage("Hết mất sách cho cậu mượn rùi T.T");
                }
            } catch (RuntimeException | IOException | SQLException e) {
                e.printStackTrace();
                CustomerAlter.showMessage("Lỗi :v");
            }
        }

    }

    public void onBookmarkButtonAction(ActionEvent actionEvent) {
        if (bookmarkButton.getText().equals("Đánh dấu")) {
            markedBook();
        } else {
            cancelMarkedBook();
        }
    }

    private void cancelMarkedBook() {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn huỷ đánh dấu sách này à?");
        if (confirmYes) {
            try {
                BookmarkController bookmarkController =
                        FXMLLoaderUtil.getInstance().getController(BOOKMARK_FXML);
                if (bookmarkController != null) {
                    bookmarkController.deleteBookmark(this.book);
                }
                Map<String, Object> criteria = new HashMap<>();
                criteria.put("ISBN", book.getISBN());
                List<BookItem> bookItem = BookItemDAO.getInstance().searchByCriteria(criteria);
                BookMark bookMark = new BookMark(UserMenuController.getMember()
                        , bookItem.getFirst());
                try {
                    BookMarkDAO.getInstance().delete(bookMark);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                BookManager.getInstance().deleteMarkedBook(bookMark);
                bookmarkButton.setText("Đánh dấu");
                CustomerAlter.showMessage("Đã huỷ đánh dấu sách nè");
            } catch (RuntimeException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void markedBook() {
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
                        , bookItem.getFirst());
                try {
                    BookMarkDAO.getInstance().add(bookMark);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                BookManager.getInstance().addMarkedBook(bookMark);
                bookmarkButton.setText("Huỷ đánh dấu");
                CustomerAlter.showMessage("Đã thêm vô đánh dấu sách nè");
            } catch (RuntimeException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Image starImage(int numOfStar) {
        System.out.println(numOfStar);
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/5Star.png"));
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

    private int fromRateToInt(String rate) {
        char firstChar = rate.charAt(0);
        if (Character.isAlphabetic(firstChar)) {
            return 0;
        }
        return Character.getNumericValue(firstChar);
    }

    public void onOpenPreviewMouseClicked(MouseEvent mouseEvent) {
        System.out.println(book.getPreview());
        openWebLink(book.getPreview());
    }

    private void openWebLink(String url) {
        try {
            // Kiểm tra nếu hệ thống hỗ trợ Desktop API
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    // Kiểm tra URL hợp lệ
                    URI uri = new URI(url);
                    desktop.browse(uri); // Mở trình duyệt với URL
                    System.out.println("Đang mở liên kết: " + url);
                } else {
                    System.out.println("Desktop không hỗ trợ mở trình duyệt.");
                }
            } else {
                System.out.println("Desktop API không được hỗ trợ trên hệ thống này.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomerAlter.showMessage("Không có preview của sách này");
            System.out.println("Lỗi khi mở liên kết: " + url);
        }
    }

}
