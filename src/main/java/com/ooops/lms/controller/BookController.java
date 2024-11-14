package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookMarkDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.*;

import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookController{
    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private Book book;

    @FXML
    VBox bookBox;

    @FXML
    private ChoiceBox<String> starChoiceBox;

    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage,starImage;

    @FXML
    private Label bookNameLabel,contentText;

    @FXML
    private VBox commentsVBox;

    private List<Comment> comments = new ArrayList<>();

    public void initialize() {
        starChoiceBox.getItems().addAll("tất cả","5 sao", "4 sao", "3 sao", "2 sao","1 sao");

        starChoiceBox.setValue("tất cả");
    }

    public void addComment() {
//        Comment comment = new Comment()
    }

    public void setData() {
        if(this.book==null) {
        }
        Image image = new Image(getClass().getResourceAsStream(book.getImagePath()));
        bookImage.setImage(image);
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        contentText.setText(book.getDescription());

        //comment
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
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/Comment-view.fxml"));
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
            // Remove content if it's already in the scene graph
            if (bookBox.getChildren().contains(content)) {
                bookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onReserveBookButtonAction(ActionEvent actionEvent) {
//        BookItem bookItem = new BookItem((int) book.getISBN(), BookItemStatus.RESERVED,"");
//        BookReservation bookReservation = new BookReservation(UserMenuController.member
//                ,bookItem, LocalDate.now().toString(),LocalDate.now().plusDays(10).toString());
//        try {
//            bookReservationDAO.add(bookReservation);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void onBookmarkButtonAction(ActionEvent actionEvent) {
        BookItem bookItem = new BookItem((int) book.getISBN(), BookItemStatus.RESERVED,"");
        BookMark bookReservation = new BookMark(UserMenuController.member
                ,bookItem);
        try {
            BookMarkDAO.getInstance().add(bookReservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
