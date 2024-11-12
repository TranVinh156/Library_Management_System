package com.ooops.lms.controller;

import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class UserMenuController implements Initializable {
    @FXML
    private VBox contentBox;

    @FXML
    private StackPane stackPane;

    @FXML
    VBox menuBar;

    @FXML
    VBox menuBarPlus;

    @FXML
    Label userNameLabel;

    @FXML
    ImageView avatarImage;

    private  FXMLLoaderUtil fxmlLoaderUtil;

    private int memberID=0;
    protected static MemberDAO memberDAO = new MemberDAO();
    protected static Member member;

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private static final String ADVANCED_SEARCH_FXML = "/com/ooops/lms/library_management_system/AdvancedSearch-view.fxml";
    private static final String BOOKMARK_FXML = "/com/ooops/lms/library_management_system/Bookmark-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String USER_LOGIN_FXML = "/com/ooops/lms/library_management_system/UserLogin.fxml";
    private static final String BOOK_RANKING_FXML = "/com/ooops/lms/library_management_system/BookRanking-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupHoverMenuBar();
        fxmlLoaderUtil = FXMLLoaderUtil.getInstance(contentBox);
        fxmlLoaderUtil.addUserMenuController(this);
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            contentBox.getChildren().clear(); // Xóa nội dung cũ, nếu có
            contentBox.getChildren().add(content);
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
            fxmlLoaderUtil.deleteAllInCache();
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

    private void setupHoverMenuBar() {
        // Hiện menuBarPlus khi di chuột vào menuBar
        menuBar.setOnMouseEntered(event ->  {
            menuBarPlus.setVisible(true);
            menuBarPlus.setPrefWidth(200);
        });


        menuBarPlus.setOnMouseExited(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1)); // thời gian chờ trước khi ẩn
            pause.setOnFinished(e -> {
                if (!menuBarPlus.isHover() && !menuBar.isHover()) {
                    menuBarPlus.setVisible(false);
                    menuBarPlus.setPrefWidth(0);
                }
            });
            pause.play();
        });
    }
    public void showInfo() {
        userNameLabel.setText(member.getPerson().getFirstName());
        String imagePath = member.getPerson().getImagePath();

        imagePath = imagePath.replace("//", "/");
        if (imagePath.contains("image/avatar/default.png")) {
            avatarImage.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));
        } else {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    avatarImage.setImage(new Image(new FileInputStream(imageFile)));
                } else {
                    System.out.println("File not found: " + imagePath);
                    avatarImage.setImage(new Image(getClass().getResourceAsStream("/image/avatar/default.png")));  // Hiển thị ảnh mặc định nếu không tìm thấy ảnh
                }
            } catch (IOException e) {
                System.out.println("Error loading image: " + e.getMessage());
                avatarImage.setImage(new Image(getClass().getResourceAsStream("/image/avatar/default.png")));  // Hiển thị ảnh mặc định nếu có lỗi
            }
        }
    }

    private void findMember() {
        try {
            member = memberDAO.find(memberID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(member != null) {
            showInfo();
        }
        else {
            System.err.println("khong tim thay member");
        }
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
        System.out.println("MemberID được thiết lập: " + memberID);
        findMember();
    }

    public static Member getMember() {
        return member;
    }

    public static void updateMember() {
        try{
            if(memberDAO.update(member)) {
                System.out.println("update succesfully");
            } else {
                System.out.println("fail to update");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
