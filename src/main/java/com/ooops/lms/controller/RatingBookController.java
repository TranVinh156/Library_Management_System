package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Comment;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import com.ooops.lms.util.ThemeManager;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingBookController {

    @FXML
    private ChoiceBox ratingChoiceBox;
    @FXML
    private VBox borrowedBookBox,ratingBox;
    @FXML
    private Label bookNameText,authorNameText,userNameText;
    @FXML
    Text descriptionText;
    @FXML
    private ImageView bookImage, starImage;
    @FXML
    private Circle avatarImage;
    @FXML
    AnchorPane shyPane, sadPane;
    @FXML
    private TextArea commentTitleText,commentContentText;

    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private BookItem currrentBookItem;
    private List<BookIssue> borrowedBookList;
    private Comment comment;
    private RatingBookCardController currentRatingBookCard;

    public void initialize() {
        ratingChoiceBox.getItems().addAll("5 sao","4 sao","3 sao","2 sao","1 sao");
        ratingChoiceBox.setValue("5 sao");
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(HISTORY_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    private void showData() {
        userNameText.setText(UserMenuController.getMember().getUsername());
        try {
            String imagePath = UserMenuController.getMember().getPerson().getImagePath();
            if (imagePath != null) {
                File file = new File(imagePath);
                Image image = new Image(file.toURI().toString());
                avatarImage.setFill(new ImagePattern(image));
            } else {
                avatarImage.setFill(Color.GRAY);
            }
        } catch (Exception e) {
            avatarImage.setFill(Color.GRAY);
            e.printStackTrace();
        }
    }

    public void showBookData(BookItem bookItem,Comment comment,RatingBookCardController ratingBookCardController) {
        commentContentText.clear();
        commentTitleText.clear();
        this.currrentBookItem = bookItem;
        this.currentRatingBookCard = ratingBookCardController;
        bookNameText.setText(bookItem.getTitle());
        String author = "tác giả: ";
        List<Author> authorList = bookItem.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameText.setText(author);
        descriptionText.setText(bookItem.getDescription());
        bookImage.setImage(new Image(bookItem.getImagePath()));
        starImage.setImage(starImage(bookItem.getRate()));

        try {
            this.comment = comment;
            commentContentText.setText(comment.getContent());
            commentTitleText.setText(comment.getTitle());
            ratingChoiceBox.setValue(comment.getRate() +" sao");
        } catch (RuntimeException e) {
            System.out.println("tao ra cmt moi");
        }
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
        if(currrentBookItem == null) {
            CustomerAlter.showMessage("Vui l chọn sách");
            return;
        }
        if(comment == null) {
            comment = new Comment(commentTitleText.getText(),commentContentText.getText()
                    ,rate(ratingChoiceBox.getValue().toString()),UserMenuController.getMember()
                    ,currrentBookItem.getISBN());
            try {
                CommentDAO.getInstance().add(comment);
                CustomerAlter.showMessage("đã lưu đánh giá");
                currentRatingBookCard.setColorGreen(comment);
                FXMLLoaderUtil.getInstance().refreshUpdateBook();
            } catch (SQLException e) {
                e.printStackTrace();
                CustomerAlter.showMessage("lỗi nè ba");
            }
        }else {
            CustomerAlter.showMessage("khong được sửa comment");
        }
    }

    public void onComplimentButtonAction(ActionEvent actionEvent) {
        sadPane.setVisible(false);

        Sound.getInstance().playSound("yourSmile.mp3");
        ThemeManager.getInstance().changeTheme("pink");
        ThemeManager.getInstance().applyTheme(ratingBox);

        shyPane.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        pause.setOnFinished(event -> {
            shyPane.setVisible(false);

        });

        pause.play();
    }

    public void onCriticizeButtonAction(ActionEvent actionEvent)  {
        shyPane.setVisible(false);

        Sound.getInstance().playSound("sad.mp3");
        ThemeManager.getInstance().changeTheme("dark");
        ThemeManager.getInstance().applyTheme(ratingBox);

//        CustomerAlter.showMessage("t ban m nè");

        sadPane.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(7));

        pause.setOnFinished(event -> {
            sadPane.setVisible(false);

        });

        pause.play();
    }

    private int rate(String rate) {
        switch (rate) {
            case "1 sao" :
                return 1;
            case "2 sao" :
                return 2;
            case "3 sao" :
                return 3;
            case "4 sao" :
                return 4;
            case "5 sao" :
                return 5;
        }
        return 5;
    }

    public void setData(List<BookIssue> bookIssueList) {
        if(this.borrowedBookList==null) {
            this.borrowedBookList = bookIssueList;
            for (int i = 0; i < bookIssueList.size(); i++) {
                loadBook(bookIssueList.get(i));
            }
        }
        showData();
    }

    public void loadBook(BookIssue bookIssue) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/RatingBookCard-view.fxml"));
            HBox cardBox = fxmlLoader.load();
            RatingBookCardController cardController = fxmlLoader.getController();
            cardController.setData(bookIssue.getBookItem());
            borrowedBookBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
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
