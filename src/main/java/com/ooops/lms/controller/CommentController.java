package com.ooops.lms.controller;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.Comment;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CommentController {
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private Comment comment;
    @FXML
    private Label nameLabel;

    @FXML
    private ImageView starImage;

    @FXML
    private Text commentText;

    @FXML
    private Circle avatarImage;
    @FXML
    VBox commentBox;

    private String [] colors = {"FFFFFF"};

    public void setData() {
        nameLabel.setText("book name");
    }

    public void setData(Comment comment,String style) {
        this.comment = comment;
        Image image = new Image(new File(UserMenuController.getMember().getPerson().getImagePath()).toURI().toString());
        avatarImage.setFill(new ImagePattern((image)));
        nameLabel.setText(comment.getMember().getUsername());
        starImage.setImage(starImage(comment.getRate()));
        commentText.setText(comment.getContent());
        commentBox.getStyleClass().clear();
        commentBox.getStyleClass().add(style);
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
