package com.ooops.lms.controller;

import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RatingBookController {

    @FXML
    private ChoiceBox ratingChoiceBox;
    @FXML
    private VBox borrowedBookBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private List<BookIssue> borrowedBookList = new ArrayList<>();


    public void initialize() {
        ratingChoiceBox.getItems().addAll("5 sao");
        ratingChoiceBox.setValue("5 sao");
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
    }

    public void setData(List<BookIssue> bookIssueList) {
        this.borrowedBookList = bookIssueList;
        for(int i = 0 ;i<bookIssueList.size();i++) {
            loadBook(bookIssueList.get(i));
        }
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
}
