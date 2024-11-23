package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.UserCommand;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookMark;
import com.ooops.lms.model.Member;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import javafx.scene.control.Button;
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


import java.io.BufferedInputStream;
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
    Button dashboardButton, bookmarkButton, bookRankingButton, settingButton, logoutButton;
    @FXML
    Button dashboardButtonPlus, bookmarkButtonPlus, bookRankingButtonPlus, settingButtonPlus, logoutButtonPlus;

    @FXML
    private ListView<HBox> suggestionList;

    private FXMLLoaderUtil fxmlLoaderUtil;
    private Button[] buttons;

    private int memberID = 0;
    private static Member member;

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";
    private static final String ADVANCED_SEARCH_FXML = "/com/ooops/lms/library_management_system/AdvancedSearch-view.fxml";
    private static final String BOOKMARK_FXML = "/com/ooops/lms/library_management_system/Bookmark-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String USER_LOGIN_FXML = "/com/ooops/lms/library_management_system/UserLogin.fxml";
    private static final String BOOK_RANKING_FXML = "/com/ooops/lms/library_management_system/BookRanking-view.fxml";
    private static final String SUGGEST_CARD_FXML = "/com/ooops/lms/library_management_system/BookSuggestionCard-view.fxml";
    private static final String MUSIC_FXML = "/com/ooops/lms/library_management_system/Music-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupHoverMenuBar();
        //set up loader and add dashboard
        fxmlLoaderUtil = FXMLLoaderUtil.getInstance(contentBox);
        fxmlLoaderUtil.addUserMenuController(this);
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            contentBox.getChildren().clear();
            contentBox.getChildren().add(content);
        }
        //theme
        ThemeManager.getInstance().addPane(stackPane);
        //search book
        searchBookSuggestion();

        buttons = new Button[]{
                dashboardButton, bookmarkButton, bookRankingButton, settingButton};
        ThemeManager.getInstance().changeMenuBarButtonColor(buttons, dashboardButton);
    }

    public void onDashboardButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, dashboardButton);
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
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, bookmarkButton);

        }
    }

    public void onSettingButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, settingButton);

        }
    }

    public void onBookRankingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(BOOK_RANKING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, bookRankingButton);

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

            fetchBooksFromApi(newValue, filteredSuggestions);

            suggestionList.setOnMouseClicked(event -> clearSuggestions(filteredSuggestions));
            suggestionContainer.setOnMouseClicked(event -> clearSuggestions(filteredSuggestions));
        });
    }

    /**
     * Phương thức gọi API để tìm kiếm sách
     */
    private void fetchBooksFromApi(String keyword, ObservableList<HBox> filteredSuggestions) {
        Task<List<Book>> fetchBooksTask = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                return BookInfoFetcher.searchBooksByKeyword(keyword);
            }
        };

        // Xử lý kết quả sau khi API trả về
        fetchBooksTask.setOnSucceeded(event -> {
            List<Book> bookList = fetchBooksTask.getValue();
            updateSuggestions(bookList, filteredSuggestions);
        });

        // Khởi chạy task trong một thread khác
        new Thread(fetchBooksTask).start();
    }

    /**
     * Cập nhật danh sách gợi ý hiển thị
     */
    private void updateSuggestions(List<Book> bookList, ObservableList<HBox> filteredSuggestions) {
        for (int i = 0; i < bookList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(SUGGEST_CARD_FXML));
                HBox cardBox = fxmlLoader.load();
                BookSuggestionCardController cardController = fxmlLoader.getController();
                cardController.setData(bookList.get(i));
                filteredSuggestions.add(cardBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 6) break; // Giới hạn hiển thị tối đa 6 gợi ý
        }

        adjustSuggestionListHeight(filteredSuggestions);
    }

    /**
     * Điều chỉnh chiều cao của danh sách gợi ý
     */
    private void adjustSuggestionListHeight(ObservableList<HBox> filteredSuggestions) {
        suggestionList.setPrefHeight(filteredSuggestions.size() * 60);
        if (suggestionList.getPrefHeight() > 360) {
            suggestionList.setPrefHeight(360);
        }
        suggestionContainer.setVisible(!filteredSuggestions.isEmpty());
        suggestionList.setVisible(!filteredSuggestions.isEmpty());
    }

    /**
     * Xóa danh sách gợi ý và làm sạch trạng thái
     */
    private void clearSuggestions(ObservableList<HBox> filteredSuggestions) {
        suggestionContainer.setVisible(false);
        filteredSuggestions.clear();
        searchText.clear();
    }


    public void onDashBoardMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, dashboardButton);
        }
    }

    public void onSettingMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, settingButton);
        }
    }

    private void setupHoverMenuBar() {
        menuBar.setOnMouseEntered(event -> {
//            Animation.getInstance().changeImage1(pullMenuBarImage);
            menuBarPlus.setVisible(true);

            // Tạo hiệu ứng tăng chiều rộng
            Timeline expandMenu = new Timeline(
                    new KeyFrame(Duration.millis(100),
                            new KeyValue(menuBarPlus.prefWidthProperty(), 350)) // Chiều rộng tối đa
            );
            expandMenu.play();
        });

        menuBarPlus.setOnMouseExited(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(e -> {
                if (!menuBarPlus.isHover() && !menuBar.isHover()) {
                    // Tạo hiệu ứng giảm chiều rộng
                    Timeline collapseMenu = new Timeline(
                            new KeyFrame(Duration.millis(50),
                                    new KeyValue(menuBarPlus.prefWidthProperty(), 0)) // Chiều rộng tối thiểu
                    );
                    collapseMenu.play();
                    collapseMenu.setOnFinished(ev -> menuBarPlus.setVisible(false));
//                    Animation.getInstance().changeImage2(pullMenuBarImage);
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
            System.out.println(file.getAbsolutePath());
            avatarImage.setImage(new Image(file.toURI().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            File file = new File("avatar/default.png");
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

    public void onMusicMouseClicked(MouseEvent mouseEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(MUSIC_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
            ThemeManager.getInstance().changeMenuBarButtonColor(buttons, settingButton);

        }
    }
}
