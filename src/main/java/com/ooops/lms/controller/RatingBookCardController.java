package com.ooops.lms.controller;

import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.*;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RatingBookCardController {
    private BookItem bookItem;

    private static final String RATING_BOOK_FXML = "/com/ooops/lms/library_management_system/RatingBook-view.fxml";
    private Comment comment;

    @FXML
    private Label bookNameText,authorNameText,statusText;
    @FXML
    private HBox ratingCardBox;


    public void setData(BookItem bookItem) {
        this.bookItem =bookItem;
        bookNameText.setText("tên sách: "+bookItem.getTitle());
        String author = "tác giả: ";
        List<Author> authorList = bookItem.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameText.setText(author);

        try {
            Map<String,Object> criteria = new HashMap<>();
            criteria.put("ISBN",bookItem.getISBN());
            criteria.put("member_ID",UserMenuController.member.getPerson().getId());
            List<Comment> commentList = CommentDAO.getInstance().searchByCriteria(criteria);
            comment=commentList.get(0);
            statusText.setText("đã đánh giá");
            ratingCardBox.setStyle("-fx-background-color: #AFFF84;-fx-background-radius: 20;");
        } catch (RuntimeException | SQLException e) {
            statusText.setText("chưa đánh giá");
            ratingCardBox.setStyle("-fx-background-color: #FF7878;-fx-background-radius: 20;");
        }
    }

    public void onShowReportMouseClicked(MouseEvent mouseEvent) {
        try {
            RatingBookController ratingBookController= FXMLLoaderUtil.getInstance().getController(RATING_BOOK_FXML);
            ratingBookController.showBookData(bookItem,comment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
