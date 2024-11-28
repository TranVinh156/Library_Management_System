package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.CommentDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Comment;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoreBookController {

    @FXML
    private VBox moreBookBox;

    private VBox test = new VBox();

    private HBox row1Box = new HBox(), row2Box=new HBox();

    private ScrollPane scrollPane1=new ScrollPane(),scrollPane2=new ScrollPane();
    @FXML
    private Pagination pagination;

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private List<Book> bookList = new ArrayList<>();

    public void initialize() {
        try {
            bookList = BookDAO.getInstance().selectAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(bookList.size());

        scrollPane1.getStyleClass().add("real-transparent-scrollpane");
        scrollPane2.getStyleClass().add("real-transparent-scrollpane");

        scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        int numberOfPage = (bookList.size() - 1) / 12 + 1;
        pagination.setPageCount((int) Math.ceil((double) bookList.size() / 12));
        pagination.setPageFactory(pageIndex -> loadBook(pageIndex * 12, Math.min((pageIndex + 1) * 12, bookList.size())));
    }

    /**
     * load các sách
     * @param start chỉ số sách đầu
     * @param end chỉ số sách cuối
     * @return VBox chứa tất cả sách
     */
    public VBox loadBook(int start, int end) {
        row1Box.getChildren().clear();
        row2Box.getChildren().clear();
        test.getChildren().clear();

        for (int i = start; i < Math.min(start + 6, end); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookList.get(i));
                row1Box.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = start + 6; i < Math.min(start + 12, end); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookList.get(i));
                row2Box.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scrollPane1.setContent(row1Box);
        scrollPane2.setContent(row2Box);
        test.getChildren().addAll(scrollPane1, scrollPane2);
        test.setAlignment(Pos.CENTER);
        return test;
    }

    /**
     * về dashboard.
     * @param event khi ấn
     */
    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            if (moreBookBox.getChildren().contains(content)) {
                moreBookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
            FXMLLoaderUtil.getInstance().changeColorWhenBack();
        }
    }

}
