package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private VBox contentBox;
    @FXML
    private HBox borrowingHBox;
    @FXML
    private HBox borrowedHBox;
    @FXML
    private HBox reservedHBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private List<BookReservation> bookReservationList;
    private BookReservationDAO bookReservationDAO = new BookReservationDAO();

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("member_ID", UserMenuController.member.getPerson().getId());
        try {
            bookReservationList = bookReservationDAO.searchByCriteria(criteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < bookReservationList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookReservationList.get(i).getBookItem());
                borrowingHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bookReservationList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData(bookReservationList.get(i).getBookItem());
                reservedHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 5; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
                VBox cardBox = fxmlLoader.load();
                BookCard2Controller cardController = fxmlLoader.getController();
                cardController.setData();
                borrowedHBox.getChildren().add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
