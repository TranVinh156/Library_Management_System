package com.ooops.lms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class BasicUserController extends BasicController {
    private static final String USER_DETAIL_FXML = "/com/ooops/lms/library_management_system/AdminUserDetail.fxml";
    protected static final String USER_TABLE_ROW_FXML = "/com/ooops/lms/library_management_system/AdminUserTableRow.fxml";
    private static final String USER_TABLE_FXML = "/com/ooops/lms/library_management_system/AdminUserTable.fxml";

    protected static final FXMLLoader userTablePaneLoader;
    protected static final FXMLLoader userDetailPaneLoader;
    protected static final Node userDetailPane;
    protected static final Node userTablePane;

    static {
        userTablePaneLoader = loadFXML(USER_TABLE_FXML, BasicUserController.class);
        userTablePane = loadPane(userTablePaneLoader, BasicController.class);

        userDetailPaneLoader = loadFXML(USER_DETAIL_FXML, BasicUserController.class);
        userDetailPane = loadPane(userDetailPaneLoader, BasicUserController.class);
    }

    /**
     * Hàm để lấy mở cửa sổ lấy ảnh.
     * @return đường dẫn của ảnh
     */
    public String getImagePath() {
        // Tạo một FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");

        // Lọc chỉ cho phép chọn các tệp ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Mở hộp thoại chọn tệp và lấy tệp đã chọn
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            return selectedFile.toURI().toString();
        } else {
            System.out.println("Không có ảnh nào được chọn.");
            return null;
        }
    }

    /**
     * hàm này sẽ kiểm tra xem đường dâẫn ảnh có tồn tại trong máy không.
     * @param path đường dẫn cần kểm tra
     * @return true nếu có, false nếu không
     */
    public boolean isValidImagePath(String path) {
        try {
            // Kiểm tra xem đường dẫn là một file hợp lệ
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                return true;
            }

            // Kiểm tra xem đường dẫn có phải là một URL hợp lệ không
            new URL(path).openConnection().connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
