package com.ooops.lms.Alter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerAlter {

    private static final String ALTER_FXML = "/com/ooops/lms/library_management_system/Alert-view.fxml";

    private static final FXMLLoader alterLoader;
    private static final AnchorPane alterPane;
    protected static AlertController alertController;
    private static Scene scene;
    static {
        //load alter
        alterLoader = new FXMLLoader(CustomerAlter.class.getResource(ALTER_FXML));
        try {
            alterPane = alterLoader.load();
            alertController = alterLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(alterPane);
    }

    /**
     * hàm dùng để mở ra cửa sổ thông báo. (Alter đã customer)
     * @param message là đoạn văn bản chính thông báo đến người dùng
     * @return true nếu người dùng xác nhận, false nếu người dùng hủy
     */
    public static boolean showAlter(String message) {
        System.out.println("Alert");
        boolean userConfirmed = false;

        try {

            alertController.setAlertMessage(message); // Thiết lập thông điệp
            alertController.reset(); // Đặt lại trạng thái trước khi hiển thị

            // Tạo Stage mới cho cảnh báo
            Stage alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Xác nhận");
            alertStage.setScene(scene);

            // Hiển thị Alert và chờ người dùng phản hồi
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.showAndWait();

            // Lấy kết quả từ controller sau khi Alert đóng
            userConfirmed = alertController.isUserConfirmed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userConfirmed;
    }

    public static void showMessage(String message) {
        try {

            alertController.setAlertMessage(message); // Thiết lập thông điệp
            alertController.reset(); // Đặt lại trạng thái trước khi hiển thị
            alertController.setNotificationMode();
            // Tạo Stage mới cho cảnh báo
            Stage alertStage = new Stage();
            alertStage.setResizable(false);
            alertStage.setTitle("Thông báo");
            alertStage.setScene(scene);

            // Hiển thị Alert và chờ người dùng phản hồi
            alertStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
