package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AdvancedSearchController implements Initializable {
    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchText;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private List<Book> findBook;

    private VBox test = new VBox();

    private HBox row1Box = new HBox(), row2Box=new HBox();

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onSearchButtonAction(ActionEvent event) {
        String keyword = searchText.getText();
        String category = categoryChoiceBox.getValue();

        if (category == null || keyword.isEmpty()) {
            System.out.println("Please select a category and enter a keyword.");
            return;
        }

        Map<String, Object> find = new HashMap<>();
        find.put(category, keyword);
        try {
            findBook = BookDAO.getInstance().searchByCriteria(find);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (findBook.isEmpty()) {
            System.out.println("No books found");
            // Có thể thông báo hoặc cập nhật UI nếu không tìm thấy sách
        } else {
            System.out.println(findBook.size());
            showFindedBook();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryChoiceBox.getItems().addAll("title","placeAt","author_name");
        categoryChoiceBox.setValue("title");

    }

    public void showFindedBook() {
        int numberOfPage = (findBook.size() - 1) / 12 + 1;
        pagination.setPageCount(numberOfPage);
        pagination.setPageFactory(pageIndex -> loadBook(pageIndex * 12, Math.min((pageIndex + 1) * 12, findBook.size())));
    }

    public VBox loadBook(int start, int end) {
        row1Box.getChildren().clear();
        row2Box.getChildren().clear();
        test.getChildren().clear();

        // Duyệt và thêm sách vào row1Box (6 sách đầu tiên)
        for (int i = start; i < Math.min(start + 6, end); i++) {
            loadBookCard(i, row1Box);
        }

        // Duyệt và thêm sách vào row2Box (6 sách tiếp theo)
        for (int i = start + 6; i < Math.min(start + 12, end); i++) {
            loadBookCard(i, row2Box);
        }

        // Thêm row1Box và row2Box vào test
        test.getChildren().addAll(row1Box, row2Box);
        return test;
    }

    private void loadBookCard(int index, HBox rowBox) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
            VBox cardBox = fxmlLoader.load();
            BookCard2Controller cardController = fxmlLoader.getController();
            cardController.setData(findBook.get(index));
            rowBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
