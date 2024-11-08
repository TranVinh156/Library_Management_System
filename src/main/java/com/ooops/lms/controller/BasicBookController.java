package com.ooops.lms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class BasicBookController extends BasicController {
    private static final String BOOK_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminBookTable.fxml";
    private static final String BOOK_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminBookDetail.fxml";
    protected static final String BOOK_TABLE_ROW_FMXL = "/com/ooops/lms/library_management_system/AdminBookTableRow.fxml";
    protected static final String BOOK_COPY_ROW_FXML = "/com/ooops/lms/library_management_system/AdminBookCopyRow.fxml";

    protected static final FXMLLoader bookTablePaneLoader;
    protected static final Node bookTablePane;
    protected static final FXMLLoader bookDetailPaneLoader;
    protected static final Node bookDetailPane;

    static {
        bookDetailPaneLoader = loadFXML(BOOK_DETAIL_FXML, BasicBookController.class);
        bookDetailPane = loadPane(bookDetailPaneLoader, BasicBookController.class);

        bookTablePaneLoader = loadFXML(BOOK_TABLE_FXML, BasicBookController.class);
        bookTablePane = loadPane(bookTablePaneLoader, BasicBookController.class);
    }

    protected void setCategoryList(Object goalFollow, HBox mainPane, AnchorPane categoryTable, VBox categoryList) {
        // Đặt sự kiện chuột cho vùng chứa chính
        mainPane.setOnMouseClicked(event -> {
            // Kiểm tra xem có nhấn vào danh sách danh mục không
            if (categoryTable.isVisible() && !categoryList.getBoundsInLocal().contains(event.getX(), event.getY())) {
                categoryTable.setVisible(false); // Ẩn danh mục nếu bấm ngoài
            }
        });

        // Thêm listener để điều chỉnh vị trí của categoryTable theo categoryFindButton
        if (goalFollow instanceof Button) {
            Button button = (Button) goalFollow;
            button.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                updateCategoryTablePosition(mainPane,goalFollow,categoryTable);
            });
        } else if (goalFollow instanceof Label) {
            Label label = (Label) goalFollow;
            label.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                updateCategoryTablePosition(mainPane,goalFollow,categoryTable);
            });
        }

        // Tạo danh sách danh mục từ một nguồn dữ liệu
        List<String> categories = List.of("Danh Mục 1", "Danh Mục 2", "Danh Mục 3");
        for (String category : categories) {
            CheckBox categoryCheckBox = new CheckBox(category);
            categoryCheckBox.setOnAction(this::onCategorySelected); // Gọi sự kiện khi nhấn checkbox
            categoryList.getChildren().add(categoryCheckBox);
        }
    }

    @FXML
    private void onCategorySelected(ActionEvent event) {
        CheckBox selectedCheckBox = (CheckBox) event.getSource();
        String categoryName = selectedCheckBox.getText();

        if (selectedCheckBox.isSelected()) {
            System.out.println("Chọn danh mục: " + categoryName);
            // Thêm logic để hiển thị hoặc xử lý sách theo danh mục được chọn
        } else {
            System.out.println("Bỏ chọn danh mục: " + categoryName);
            // Thêm logic để ẩn hoặc xử lý sách khi danh mục không còn được chọn
        }
    }

    public void updateCategoryTablePosition(Object mainPane, Object goalFollow, Object follower) {
        // Lấy tọa độ X,Y của categoryFindButton trong hệ tọa độ scene
        double buttonSceneX = 0, buttonSceneY = 0, mainPaneSceneX = 0, mainPaneSceneY = 0;
        if (goalFollow instanceof Button) {
            Button button = (Button) goalFollow;
            Point2D buttonCoords = button.localToScene(0, 0);
            buttonSceneX = buttonCoords.getX(); // tọa độ X của button trong không gian cảnh
            buttonSceneY = buttonCoords.getY(); // tọa độ Y của button trong không gian cảnh
        } else if (goalFollow instanceof Label) {
            Label label = (Label) goalFollow;
            Point2D buttonCoords = label.localToScene(0, 0);
            buttonSceneX = buttonCoords.getX();
            buttonSceneY = buttonCoords.getY();
        }

        // Lấy tọa độ X,Y của mainPane trong hệ tọa độ scene
        if (mainPane instanceof HBox) {
            HBox mainPaneHBox = (HBox) mainPane;
            Point2D mainPaneCoords = mainPaneHBox.localToScene(0, 0);
            mainPaneSceneX = mainPaneCoords.getX(); // tọa độ X của mainPane trong không gian cảnh
            mainPaneSceneY = mainPaneCoords.getY(); // tọa độ Y của mainPane trong không gian cảnh
        }
        // Tính toán tọa độ X của categoryFindButton so với mainPane
        double buttonRelativeX = buttonSceneX - mainPaneSceneX;
        double buttonRelativeY = buttonSceneY - mainPaneSceneY; // nếu cần tính tọa độ Y, có thể thêm

        // In ra tọa độ X và Y tương đối
        System.out.println("Tọa độ X của categoryFindButton so với mainPane: " + buttonRelativeX);
        System.out.println("Tọa độ Y của categoryFindButton so với mainPane: " + buttonRelativeY);

        if (follower instanceof AnchorPane) {
            AnchorPane categoryTable = (AnchorPane) follower;
            // Cập nhật vị trí cho categoryTable (nếu cần)
            categoryTable.setLayoutX(buttonRelativeX);
            // Nếu cần cập nhật vị trí Y, có thể thêm:
            categoryTable.setLayoutY(buttonRelativeY + 33);
        }
    }

}
