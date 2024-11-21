package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public abstract class BasePageController<T, D extends BaseDetailController<T>,
        TB extends BaseTableController<T, ? extends BasePageController, ? extends BaseRowController>>
        extends BasicController {

    @FXML
    protected Label titlePage;

    protected FXMLLoader detailLoader;
    protected FXMLLoader tableLoader;
    protected Node detailPane;
    protected Node tablePane;

    protected D detailController;
    protected TB tableController;


    protected abstract String getDetailFXMLPath();
    protected abstract String getTableFXMLPath();

    protected boolean page1 = true;

    @FXML
    public void initialize() {
        try {
            detailLoader = loadFXML(getDetailFXMLPath(),BaseDetailController.class);
            detailPane = loadPane(detailLoader,BaseDetailController.class);
            detailController = detailLoader.getController();
            detailController.setMainController(this);
            if(detailLoader == null){
                System.out.println("detailLoader is null");
            }

            tableLoader = loadFXML(getTableFXMLPath(),BaseTableController.class);
            tablePane = loadPane(tableLoader,BaseTableController.class);
            tableController = tableLoader.getController();
            tableController.setMainController(this);

            setupControllers();
            setupViews();
            startPage();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void setupControllers();

    protected abstract void setupViews();

    public void loadDetail(T item) {
        if (detailController.hasUnsavedChanges()) {
            boolean confirmYes = CustomerAlter.showAlter("Thông tin bạn đang thêm/sửa sẽ bị mất");
            if (confirmYes) {
                loadDetailItem(item);
            }
        } else {
            loadDetailItem(item);
        }
    }

    protected void loadDetailItem(T item) {
        detailController.loadStartStatus();
        detailController.setItem(item);
        if(page1) {
            alterPage();
        }
    }
    public void loadAddPane() {
        getTitlePageStack().push("Add");
        if (detailController.hasUnsavedChanges()) {
            boolean confirmYes = CustomerAlter.showAlter("Thông tin bạn đang thêm/sửa sẽ bị mất");
            if (confirmYes) {
                loadAddNewItem();
            }
        } else {
            loadAddNewItem();
        }
    }
    protected void loadAddNewItem() {
        detailController.loadStartStatus();
        detailController.setAddMode(true);
        if(page1) {
            alterPage();
        }
    }

    public abstract void alterPage();

    public abstract void startPage();

    public void loadData() {
        setTitlePage();
        tableController.loadData();
    }

    protected void setTitlePage(){
        titlePage.setText(String.join(" / ", getTitlePageStack()));
    }


}
