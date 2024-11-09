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
    private HBox hBox1;

    @FXML
    private HBox hBox2;

    @FXML
    private HBox hBox3;

    @FXML
    private TextField searchText;

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private BookDAO bookDAO = new BookDAO();

    private List<Book> findBook;

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
        Map<String,Object> find = new HashMap<>();
        find.put(category,keyword);
        try {
            findBook =bookDAO.searchByCriteria(find);
        } catch (SQLException e) {

        }
        showFindedBook();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryChoiceBox.getItems().addAll("tất cả","title", "mới nhất", "dở nhất", "tác giả","không phải sách","210");

        categoryChoiceBox.setValue("tất cả");

    }

    private void showFindedBook() {
        if(findBook==null || findBook.isEmpty()) return;
        try {
            for(int i = 0;i<2;i++) {
                if(i==findBook.size()) return;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData(findBook.get(i));
                    hBox1.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for(int i = 2;i<4;i++) {
                if(i==findBook.size()) return;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData();
                    hBox2.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for(int i = 4;i<6;i++) {
                if(i==findBook.size()) return;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard1-view.fxml"));
                    HBox cardBox = fxmlLoader.load();
                    BookCard1Controller cardController = fxmlLoader.getController();
                    cardController.setData();
                    hBox3.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
