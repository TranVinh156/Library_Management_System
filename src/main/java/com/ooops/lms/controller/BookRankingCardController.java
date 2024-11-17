package com.ooops.lms.controller;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BookRankingCardController {
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String BOOK_FXML = "/com/ooops/lms/library_management_system/Book-view.fxml";

    private Book book;

    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label ranking;

    @FXML
    private HBox hBox;

    @FXML
    private ImageView starImage;

    public void setData(Book book,String rank) {
        this.book = book;
        Image image = new Image(getClass().getResourceAsStream("/"+book.getImagePath()));
        bookImage.setImage(image);
        bookNameLabel.setText("book name");
        authorNameLabel.setText("author");
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        ranking.setText(rank);
    }

    public void onReadButtonAction(ActionEvent actionEvent) {
        try {
            // Load FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(BOOK_FXML);
            fxmlLoader.setLocation(resource);

            // Load the content before getting the controller
            VBox newContent = fxmlLoader.load();

            // Lấy controller và set book
            BookController bookController = fxmlLoader.getController();
            if (book != null) {
                bookController.setBook(book);  // Chắc chắn rằng book không phải là null tại đây
                bookController.setData(); // Gọi setData() ngay sau khi setBook()
            } else {
                System.err.println("Book object is null!");
            }

            // Cập nhật container với nội dung FXML mới
            fxmlLoaderUtil.updateContentBox(newContent);

        } catch (IOException e) {
            e.printStackTrace();  // In ra lỗi nếu gặp ngoại lệ
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
}
