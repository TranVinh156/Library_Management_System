package com.ooops.lms.controller;

import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Comment;
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
import java.util.List;

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
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private HBox hBox;

    @FXML
    private Label contentText;

    @FXML
    private ImageView starImage;

    @FXML
    private VBox commentsVBox;

    @FXML
    TextField commentTextfield;

    private List<Comment> comments;
    private CommentDAO commentDAO;


    public void initialize() {
        starChoiceBox.getItems().addAll("tất cả","5 sao", "4 sao", "3 sao", "2 sao","1 sao");

        starChoiceBox.setValue("tất cả");

        commentDAO = new CommentDAO();
        commentTextfield.setOnAction(event -> {
            commentTextfield.clear();
        });

        try {
            comments = commentDAO.selectAll();
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
            if (i == 9) {
                break;
            }
        }
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
    }

    public void onBookmarkButtonAction(ActionEvent actionEvent) {
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
