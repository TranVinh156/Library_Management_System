package com.ooops.lms.controller;

import com.ooops.lms.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AdminBookPageController extends BasicBookController {

    @FXML
    private AnchorPane detailLocation;

    @FXML
    private AnchorPane detailPage;

    @FXML
    private Button returnButton;

    @FXML
    private AnchorPane tablePage;

    private AdminBookDetailController adminBookDetailController;
    private AdminBookTableController adminBookTableController;
    private AdminMenuController mainController;

    public void setMainController( AdminMenuController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        // lấy BookDetail và gán mainController của bookDetail
        adminBookDetailController = bookDetailPaneLoader.getController();
        adminBookDetailController.setMainController(this);

        // lấy BookTable và gán mainController của bookTable
        adminBookTableController = bookTablePaneLoader.getController();
        adminBookTableController.setMainController(this);

        //gán các fxml vào với nhau
        tablePage.getChildren().add(bookTablePane);
        detailLocation.getChildren().add(bookDetailPane);
    }

    /**
     * Khi mà bấm quay lại (page1 -> page2) thì sẽ load lại Data của bảng Table và các Summary Total
     *
     * @param event
     */
    @FXML
    void onReturnButton(ActionEvent event) {
        loadData();
        alterPage();
    }

    /**
     * Khi mà bấm vào ROW thì sẽ chuyển sang Page2 (Detail)
     * Load detail Book của ROW đấy.
     *
     * @param book
     */
    public void loadDetail(Book book) {
        adminBookDetailController.loadStartStatus();
        adminBookDetailController.setItem(book);
        alterPage();
    }

    /**
     * Khi bấm nút thêm sách thì sẽ load mới detail trở về trạng thái StartStatus
     * Lúc đấy bảng Detail sẽ có Mode là addMode.
     */
    public void loadAddPane() {
        adminBookDetailController.loadStartStatus();
        adminBookDetailController.setAddMode(true);
        alterPage();
    }

    /**
     * Chuyển trang.
     */
    public void alterPage() {
        detailPage.setVisible(!detailPage.isVisible());
        tablePage.setVisible(!tablePage.isVisible());
    }
    public void startPage() {
        detailPage.setVisible(false);
        tablePage.setVisible(true);
    }

    /**
     * Hàm này dùng để gọi loadData của BookTable.
     * Load lại các hàng trong bảng.
     */
    public void loadData() {
        adminBookTableController.loadData();
    }

}
