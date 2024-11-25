package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class CategoryController {
    @FXML
    Label categoryName;
    @FXML
    Button XButton;

    private AdvancedSearchController advancedSearchController;
    public void setData(String name,AdvancedSearchController advancedSearchController) {
        this.advancedSearchController = advancedSearchController;
        categoryName.setText(name);
        categoryName.getStyleClass().add("label-4");
    }

    public void onCategoryMouseClicked(MouseEvent mouseEvent) {
        categoryName.getStyleClass().clear();
        categoryName.getStyleClass().add("label-5");
        XButton.setVisible(true);
        advancedSearchController.addCategoryCriteria(categoryName.getText());
    }

    public void onXbuttonAction(ActionEvent actionEvent) {
        categoryName.getStyleClass().clear();
        categoryName.getStyleClass().add("label-4");
        XButton.setVisible(false);
        advancedSearchController.deleteCategoryCriteria(categoryName.getText());
    }
}
