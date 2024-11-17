package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.UserCommand;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Member;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.concurrent.Task;


import java.io.File;
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

    @FXML
    TextField searchText;

    @FXML
    AnchorPane suggestionContainer;

    @FXML
    private ListView<HBox> suggestionList;

    private FXMLLoaderUtil fxmlLoaderUtil;

    private int memberID = 0;
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
        searchBookSuggestion();
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
        boolean confirmYes = CustomerAlter.showAlter("Anh bỏ em à");
        if (confirmYes) {
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
    }

    public void searchBookSuggestion() {
        ObservableList<HBox> filteredSuggestions = FXCollections.observableArrayList();
        suggestionList.setItems(filteredSuggestions);

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredSuggestions.clear();

            if (!newValue.isEmpty()) {
                // Tạo Task để tìm kiếm sách bất đồng bộ
                Task<List<Book>> fetchBooksTask = new Task<>() {
                    @Override
                    protected List<Book> call() throws Exception {
                        // Thực hiện tìm kiếm sách trên một luồng riêng
                        return BookInfoFetcher.searchBooksByKeyword(searchText.getText());
                    }
                };

                // Khi dữ liệu được trả về, cập nhật giao diện trên JavaFX Application Thread
                fetchBooksTask.setOnSucceeded(event -> {
                    List<Book> bookList = fetchBooksTask.getValue();

                    for (int i = 0; i < bookList.size(); i++) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookSuggestionCard-view.fxml"));
                            HBox cardBox = fxmlLoader.load();
                            BookSuggestionCardController cardController = fxmlLoader.getController();
                            cardController.setData(bookList.get(i));
                            filteredSuggestions.add(cardBox);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (i == 6) {
                            break;
                        }
                    }

                    suggestionList.setPrefHeight(filteredSuggestions.size() * 60);
                    if (suggestionList.getPrefHeight() > 360) {
                        suggestionList.setPrefHeight(360);
                    }
                    suggestionContainer.setVisible(!filteredSuggestions.isEmpty());
                    suggestionList.setVisible(!filteredSuggestions.isEmpty());
                });

                // Thực thi task
                new Thread(fetchBooksTask).start();
            }

            // Các sự kiện để ẩn hoặc xóa gợi ý khi người dùng bấm vào
            suggestionList.setOnMouseClicked(event -> {
                suggestionContainer.setVisible(false);
                filteredSuggestions.clear();
                searchText.clear();
            });

            suggestionContainer.setOnMouseClicked(event -> {
                searchText.clear();
                suggestionContainer.setVisible(false);
                filteredSuggestions.clear();
            });
        });
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
        menuBar.setOnMouseEntered(event -> {
            menuBarPlus.setVisible(true);
            menuBarPlus.setPrefWidth(350);
        });

        menuBarPlus.setOnMouseExited(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
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

        try {
            File file = new File(imagePath);
            avatarImage.setImage(new Image(file.toURI().toString()));
        } catch (Exception e) {
            member.getPerson().setImagePath("Library_Management_System/avatar/default.png");
            File file = new File("Library_Management_System/avatar/default.png");
            avatarImage.setImage(new Image(file.toURI().toString()));
        }
    }

    private void findMember() {
        try {
            member = MemberDAO.getInstance().find(memberID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (member != null) {
            showInfo();
        } else {
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

}
