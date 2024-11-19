package com.ooops.lms.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public abstract class BaseRowController<T, P extends BasePageController> extends BasicController{
    @FXML
    protected HBox mainRowHbox;
    protected T item;
    protected P mainController;

    protected static BaseRowController selectedRow = null;

    public void setMainController(P controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        setupRowClickHandler();
    }

    private void setupRowClickHandler() {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleRowClick();
            }
        });
    }

    protected void handleRowClick() {
        updateRowSelection();
        loadDetailView();
    }

    protected void updateRowSelection() {
        if (selectedRow != null) {
            selectedRow.resetRowStyle();
        }
        selectedRow = this;
        setSelectedRowStyle();
    }
    protected void setSelectedRowStyle() {
        mainRowHbox.setStyle("-fx-background-color: #DDDCDC;");
    }
    public void resetRowStyle() {
        mainRowHbox.setStyle("");
    }

    public void setItem(T item) {
        this.item = item;
        updateRowDisplay();
    }

    protected void loadDetailView() {
        mainController.loadDetail(item);
    };
    protected abstract void updateRowDisplay();

}

