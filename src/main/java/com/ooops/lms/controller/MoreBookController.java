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

    public VBox loadBook(int start, int end) {
        // Xóa nội dung cũ để không lặp lại khi chuyển trang
        row1Box.getChildren().clear();
        row2Box.getChildren().clear();
        test.getChildren().clear();

        // Duyệt và thêm sách vào row1Box
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

        // Duyệt và thêm sách vào row2Box
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
        // Thêm row1Box và row2Box vào test
        scrollPane1.setContent(row1Box);
        scrollPane2.setContent(row2Box);
        test.getChildren().addAll(scrollPane1, scrollPane2);
        test.setAlignment(Pos.CENTER);
        return test;
    }


    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            // Remove content if it's already in the scene graph
            if (moreBookBox.getChildren().contains(content)) {
                moreBookBox.getChildren().remove(content);
            }
            fxmlLoaderUtil.updateContentBox(content);
            FXMLLoaderUtil.getInstance().changeColorWhenBack();
        }
    }

}
