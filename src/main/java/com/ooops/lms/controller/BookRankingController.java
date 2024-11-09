package com.ooops.lms.controller;

import com.ooops.lms.model.Book;
import com.ooops.lms.util.BookManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookRankingController implements Initializable {

    @FXML
    VBox rankingVBox;

    private BookManager bookManager = BookManager.getInstance();

    private List<Book> highRankBooks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        highRankBooks = bookManager.getHighRankBooks();

        for (int i = 0; i < highRankBooks.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookRankingCard-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                BookRankingCardController cardController = fxmlLoader.getController();
                cardController.setData(highRankBooks.get(i),String.valueOf(i+1));
                rankingVBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i == 5) break;
        }
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
    }
}
