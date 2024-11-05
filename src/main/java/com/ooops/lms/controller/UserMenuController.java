package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.*;

public class UserMenuController implements Initializable {
    @FXML
    private VBox contentBox;

    @FXML
    private StackPane stackPane;

    private  FXMLLoaderUtil fxmlLoaderUtil;

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private static final String ADVANCED_SEARCH_FXML = "/com/ooops/lms/library_management_system/AdvancedSearch-view.fxml";
    private static final String BOOKMARK_FXML = "/com/ooops/lms/library_management_system/Bookmark-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String USER_LOGIN_FXML = "/com/ooops/lms/library_management_system/UserLogin-view.fxml";
    private static final String BOOK_RANKING_FXML = "/com/ooops/lms/library_management_system/BookRanking-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxmlLoaderUtil = FXMLLoaderUtil.getInstance(contentBox);
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
        ThemeManager.getInstance().addPane(stackPane);
    }

    public void onDashboardButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onAdvancedSearchButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(ADVANCED_SEARCH_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onBookmarkButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(BOOKMARK_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onSettingButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onBookRankingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(BOOK_RANKING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onLogoutButtonAction(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(USER_LOGIN_FXML));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSearchTextAction(ActionEvent actionEvent) {
    }

    public void onDashBoardMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onSettingMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }
}
