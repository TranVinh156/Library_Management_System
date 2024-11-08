package com.ooops.lms.controller;

import com.ooops.lms.Command.CommandInvoker;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BasicController  {
    private static final String DEFAULT_USER_FXML ="/image/customer/menu/ava.png";
    private static final String LOGIN_FXML = "/com/ooops/lms/library_management_system/UserLogin.fxml";
    private static final String RESIGN_FXML = "/com/ooops/lms/library_management_system/UserResign-view.fxml";
    private static final String FORGOT_PASSWORD_FXML = "/com/ooops/lms/library_management_system/ForgotPassword-view.fxml";

    private static final String ADMIN_MENU_FXML = "/com/ooops/lms/library_management_system/AdminMenu.fxml";

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/AdminDashBoardMain.fxml";
    protected static final String TOPBOOK_CARD_FXML = "/com/ooops/lms/library_management_system/TopBookCard.fxml";
    protected static final String ISSUE_RECENT_ROW_FXML = "/com/ooops/lms/library_management_system/AdminRecentIssuelTableRow.fxml";

    protected static final String MESSAGE_FXML = "/com/ooops/lms/library_management_system/AdminMessage.fxml";

    private static final String BOOK_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminBookPage.fxml";
    private static final String USER_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminUserPage.fxml";
    private static final String ISSUE_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminIssuePage.fxml";
    private static final String BORROW_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminBorrowPage.fxml";

    protected static final FXMLLoader loginLoader;
    protected static final Node loginPane;

    protected static Image defaultUserImage;

    protected static final FXMLLoader dashboardLoader;
    protected static final AnchorPane dashboardPane;

    protected static final FXMLLoader topBookCardLoader;
    protected static final Node topBookCardPane;
    protected static final FXMLLoader recentIssueRowPaneLoader;
    protected static final Node recentIssueRowPane;

    protected static final FXMLLoader messageLoader;
    protected static final Node messagePane;

    protected static final FXMLLoader userPagePaneLoader;
    protected static final Node userPagePane;

    protected static final FXMLLoader issuePagePaneLoader;
    protected static final Node issuePagePane;

    protected static final FXMLLoader borrowPagePaneLoader;
    protected static final Node borrowPagePane;

    protected static final FXMLLoader bookPagePaneLoader;
    protected static final Node bookPagePane;

    protected CommandInvoker commandInvoker = new CommandInvoker();
    protected BookDAO bookDAO = new BookDAO();
    protected MemberDAO memberDAO = new MemberDAO();

    static {
        //load login
        loginLoader = loadFXML(LOGIN_FXML,BasicController.class);
        loginPane = loadPane(loginLoader,BasicController.class);

        //load default image
        defaultUserImage = new Image(BasicController.class.getResource(DEFAULT_USER_FXML).toExternalForm());

        //load dashboard
        dashboardLoader = loadFXML(DASHBOARD_FXML, BasicController.class);
        dashboardPane = loadPane(dashboardLoader, BasicController.class);

        //load some Item
        messageLoader = loadFXML(MESSAGE_FXML, BasicController.class);
        messagePane = loadPane(messageLoader, BasicController.class);
        topBookCardLoader = loadFXML(TOPBOOK_CARD_FXML, BasicController.class);
        topBookCardPane = loadPane(topBookCardLoader, BasicController.class);
        recentIssueRowPaneLoader = loadFXML(ISSUE_RECENT_ROW_FXML, BasicController.class);
        recentIssueRowPane = loadPane(recentIssueRowPaneLoader, BasicController.class);


        //load BookManagement-view
        bookPagePaneLoader = loadFXML(BOOK_PAGE_FXML, BasicController.class);
        bookPagePane = loadPane(bookPagePaneLoader, BasicController.class);

        //load UserManagement-view
        userPagePaneLoader = loadFXML(USER_PAGE_FXML, BasicController.class);
        userPagePane = loadPane(userPagePaneLoader, BasicController.class);

        //load IssueManagement-view
        issuePagePaneLoader = loadFXML(ISSUE_PAGE_FXML, BasicController.class);
        issuePagePane = loadPane(issuePagePaneLoader, BasicController.class);

        //load BorrowMangament-view
        borrowPagePaneLoader = loadFXML(BORROW_PAGE_FXML,BasicController.class);
        borrowPagePane = loadPane(borrowPagePaneLoader, BasicController.class);
    }

    /**
     * hàm load Pane
     * @param fxml
     * @param clazz
     * @return pane
     * @param <T>
     */
    public static <T> T loadPane(FXMLLoader fxml, Class<?> clazz) {
        T pane = null; // Khai báo biến pane kiểu T

        try {
            pane = fxml.load(); // Tải FXML và gán cho pane
            System.out.println("Successfully loaded FXML: " + fxml);

            // Thiết lập các ràng buộc cho AnchorPane (nếu T là AnchorPane)
            if (pane instanceof AnchorPane) {
                AnchorPane.setTopAnchor((AnchorPane) pane, 0.0);
                AnchorPane.setBottomAnchor((AnchorPane) pane, 0.0);
                AnchorPane.setLeftAnchor((AnchorPane) pane, 0.0);
                AnchorPane.setRightAnchor((AnchorPane) pane, 0.0);
            } else if (pane instanceof HBox) {
                AnchorPane.setTopAnchor((HBox)pane, 0.0);
                AnchorPane.setBottomAnchor((HBox)pane, 0.0);
                AnchorPane.setLeftAnchor((HBox)pane, 0.0);
                AnchorPane.setRightAnchor((HBox)pane, 0.0);
            } else if (pane instanceof VBox) {
                AnchorPane.setTopAnchor((VBox)pane, 0.0);
                AnchorPane.setBottomAnchor((VBox)pane, 0.0);
                AnchorPane.setLeftAnchor((VBox)pane, 0.0);
                AnchorPane.setRightAnchor((VBox)pane, 0.0);
            }

        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxml);
            e.printStackTrace();
        }
        return pane; // Trả về pane
    }

    /**
     * hàm này dùng để loadFXML.
     * @param fxml đường dẫn của FXML
     * @param clazz
     * @return FXMLLoader yêu cầu
     */
    public static FXMLLoader loadFXML(String fxml, Class<?> clazz) {
        return new FXMLLoader(clazz.getResource(fxml));
    }

    /**
     * hàm để lấy Controller
     * @param loader FXMLLoader cần lấy controller
     * @param clazz nhập vào class
     * @return controller cần lấy
     * @param <T> lớp
     */
    private static <T> T loadController(FXMLLoader loader, Class<?> clazz) {
        try {
            Node node = loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * hàm dùng để set chiều rộng của child theo parent.
     * @param child có thể là VBox, HBox
     * @param parent là scrollPane
     * @param <T> thuộc tính của child
     * @param <U> thuộc tính của parent
     */
    public <T,U> void childFitWidthParent(T child, U parent) {
        if (child instanceof VBox && parent instanceof ScrollPane) {
            VBox vboxChild = (VBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            vboxChild.prefWidthProperty().bind(scrollPaneParent.widthProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof ScrollPane) {
            HBox hBoxChild = (HBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            hBoxChild.prefWidthProperty().bind(scrollPaneParent.widthProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof VBox ) {
            HBox hBoxChild = (HBox) child;
            VBox vboxChild = (VBox) parent;

            hBoxChild.prefWidthProperty().bind(vboxChild.widthProperty().subtract(16));
        }

    }

    /**
     * hàm dùng để set chiều cao của child theo parent.
     * @param child có thể là VBox, HBox
     * @param parent là scrollPane
     * @param <T> thuộc tính của child
     * @param <U> thuộc tính của parent
     */
    public <T,U> void childFitHeightParent(T child, U parent) {
        if (child instanceof VBox && parent instanceof ScrollPane) {
            VBox vboxChild = (VBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            vboxChild.prefHeightProperty().bind(scrollPaneParent.heightProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof ScrollPane) {
            HBox hBoxChild = (HBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            hBoxChild.prefHeightProperty().bind(scrollPaneParent.heightProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof VBox ) {
            HBox hBoxChild = (HBox) child;
            VBox vboxChild = (VBox) parent;

            hBoxChild.prefHeightProperty().bind(vboxChild.heightProperty().subtract(16));
        }

    }


    public String getCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return "Không có danh mục"; // Trả về thông báo nếu không có danh mục
        }

        StringBuilder result = new StringBuilder(); // Sử dụng StringBuilder để xây dựng chuỗi

        for (int i = 0; i < categories.size(); i++) {
            // Giả sử mỗi Category có phương thức getName()
            result.append(categories.get(i).getCatagoryName());

            // Nếu không phải là phần tử cuối cùng, thêm dấu phẩy
            if (i < categories.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString(); // Chuyển đổi StringBuilder thành String
    }

    public String getAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return "Không có danh mục"; // Trả về thông báo nếu không có danh mục
        }

        StringBuilder result = new StringBuilder(); // Sử dụng StringBuilder để xây dựng chuỗi

        for (int i = 0; i < authors.size(); i++) {
            result.append(authors.get(i).getAuthorName());

            if (i < authors.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString(); // Chuyển đổi StringBuilder thành String
    }
}

