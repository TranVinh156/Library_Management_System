package com.ooops.lms.controller;

import com.google.api.services.books.v1.model.Volumeseriesinfo;
import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.CommandInvoker;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class BasicController {
    private static final String DEFAULT_USER_FXML = "/image/customer/menu/ava.png";
    private static final String LOGIN_FXML = "/com/ooops/lms/library_management_system/UserLogin.fxml";
    private static final String RESIGN_FXML = "/com/ooops/lms/library_management_system/UserResign-view.fxml";
    private static final String FORGOT_PASSWORD_FXML = "/com/ooops/lms/library_management_system/ForgotPassword-view.fxml";

    protected static final String ADMIN_MENU_FXML = "/com/ooops/lms/library_management_system/AdminMenu.fxml";

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/AdminDashBoardMain.fxml";
    protected static final String TOPBOOK_CARD_FXML = "/com/ooops/lms/library_management_system/TopBookCard.fxml";
    protected static final String ISSUE_RECENT_ROW_FXML = "/com/ooops/lms/library_management_system/AdminRecentIssuelTableRow.fxml";

    protected static final String MESSAGE_FXML = "/com/ooops/lms/library_management_system/AdminMessage.fxml";

    private static final String BOOK_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminBookPage.fxml";
    private static final String USER_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminUserPage.fxml";
    private static final String ISSUE_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminIssuePage.fxml";
    private static final String BORROW_PAGE_FXML = "/com/ooops/lms/library_management_system/AdminBorrowPage.fxml";
    private static final String RESERVATION_PAGE_FXML ="/com/ooops/lms/library_management_system/AdminReservationPage.fxml";

    private static Stack<String> titlePageStack = new Stack<>();

    protected static final FXMLLoader loginLoader;
    protected static final Node loginPane;

    protected static final FXMLLoader adminMenuPaneLoader;
    protected static final Node adminMenuPane;

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

    protected static final FXMLLoader reservationPagePaneLoader;
    protected static final Node reservationPagePane;

    protected CommandInvoker commandInvoker = new CommandInvoker();

    static {
        //load login
        loginLoader = loadFXML(LOGIN_FXML, BasicController.class);
        loginPane = loadPane(loginLoader, BasicController.class);

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
        borrowPagePaneLoader = loadFXML(BORROW_PAGE_FXML, BasicController.class);
        borrowPagePane = loadPane(borrowPagePaneLoader, BasicController.class);

        //load Reservation-view
        reservationPagePaneLoader = loadFXML(RESERVATION_PAGE_FXML, BasicController.class);
        reservationPagePane = loadPane(reservationPagePaneLoader, BasicController.class);

        adminMenuPaneLoader = loadFXML(ADMIN_MENU_FXML,BasicController.class);
        adminMenuPane = loadPane(adminMenuPaneLoader, BasicController.class);
    }

    // Lấy tất cả tiêu đề và kết hợp thành một chuỗi
    public String getAllTitles() {
        return String.join(" / ", titlePageStack);
    }

    public Stack<String> getTitlePageStack() {
        return titlePageStack;
    }

    /**
     * hàm load Pane
     *
     * @param fxml
     * @param clazz
     * @param <T>
     * @return pane
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
                AnchorPane.setTopAnchor((HBox) pane, 0.0);
                AnchorPane.setBottomAnchor((HBox) pane, 0.0);
                AnchorPane.setLeftAnchor((HBox) pane, 0.0);
                AnchorPane.setRightAnchor((HBox) pane, 0.0);
            } else if (pane instanceof VBox) {
                AnchorPane.setTopAnchor((VBox) pane, 0.0);
                AnchorPane.setBottomAnchor((VBox) pane, 0.0);
                AnchorPane.setLeftAnchor((VBox) pane, 0.0);
                AnchorPane.setRightAnchor((VBox) pane, 0.0);
            }

        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxml);
            e.printStackTrace();
        }
        return pane; // Trả về pane
    }

    /**
     * hàm này dùng để loadFXML.
     *
     * @param fxml  đường dẫn của FXML
     * @param clazz
     * @return FXMLLoader yêu cầu
     */
    public static FXMLLoader loadFXML(String fxml, Class<?> clazz) {
        return new FXMLLoader(clazz.getResource(fxml));
    }

    /**
     * hàm để lấy Controller
     *
     * @param loader FXMLLoader cần lấy controller
     * @param clazz  nhập vào class
     * @param <T>    lớp
     * @return controller cần lấy
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
     *
     * @param child  có thể là VBox, HBox
     * @param parent là scrollPane
     * @param <T>    thuộc tính của child
     * @param <U>    thuộc tính của parent
     */
    public <T, U> void childFitWidthParent(T child, U parent) {
        if (child instanceof VBox && parent instanceof ScrollPane) {
            VBox vboxChild = (VBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            vboxChild.prefWidthProperty().bind(scrollPaneParent.widthProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof ScrollPane) {
            HBox hBoxChild = (HBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            hBoxChild.prefWidthProperty().bind(scrollPaneParent.widthProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof VBox) {
            HBox hBoxChild = (HBox) child;
            VBox vboxChild = (VBox) parent;

            hBoxChild.prefWidthProperty().bind(vboxChild.widthProperty().subtract(16));
        } else if (child instanceof AnchorPane && parent instanceof ScrollPane) {
            AnchorPane anchorPaneChild = (AnchorPane) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            anchorPaneChild.prefWidthProperty().bind(scrollPaneParent.widthProperty());
        } else if (child instanceof AnchorPane && parent instanceof VBox) {
            AnchorPane anchorPaneChild = (AnchorPane) child;
            VBox vboxChild = (VBox) parent;
            anchorPaneChild.prefWidthProperty().bind(vboxChild.widthProperty());
        }

    }

    /**
     * hàm dùng để set chiều cao của child theo parent.
     *
     * @param child  có thể là VBox, HBox
     * @param parent là scrollPane
     * @param <T>    thuộc tính của child
     * @param <U>    thuộc tính của parent
     */
    public <T, U> void childFitHeightParent(T child, U parent) {
        if (child instanceof VBox && parent instanceof ScrollPane) {
            VBox vboxChild = (VBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            vboxChild.prefHeightProperty().bind(scrollPaneParent.heightProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof ScrollPane) {
            HBox hBoxChild = (HBox) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            hBoxChild.prefHeightProperty().bind(scrollPaneParent.heightProperty().subtract(16));
        } else if (child instanceof HBox && parent instanceof VBox) {
            HBox hBoxChild = (HBox) child;
            VBox vboxChild = (VBox) parent;

            hBoxChild.prefHeightProperty().bind(vboxChild.heightProperty().subtract(16));
        } else if (child instanceof AnchorPane && parent instanceof ScrollPane) {
            AnchorPane anchorPaneChild = (AnchorPane) child;
            ScrollPane scrollPaneParent = (ScrollPane) parent;

            anchorPaneChild.prefHeightProperty().bind(scrollPaneParent.widthProperty().subtract(17));
        } else if(child instanceof AnchorPane && parent instanceof VBox) {
            AnchorPane anchorPaneChild = (AnchorPane) child;
            VBox vboxChild = (VBox) parent;
            anchorPaneChild.prefHeightProperty().bind(vboxChild.heightProperty());
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

    /**
     * Hàm để lấy mở cửa sổ lấy ảnh.
     *
     * @return đường dẫn của ảnh
     */
    public String getImagePath(Object o) {
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
            // Tạo tên tệp mới dựa trên ID người dùng
            String imageFile = o.toString() + getFileExtension(selectedFile.toPath());
            //newImageFile = "Library_Management_System/avatar/" + imageFile;
            Path avatarFolder =Paths.get("");
            if(o instanceof Member) {
                avatarFolder = Paths.get("Library_Management_System/avatar");
            } else {
                avatarFolder = Paths.get("Library_Management_System/avatar");
            }
            try {
                // Tạo thư mục nếu chưa tồn tại
                if (Files.notExists(avatarFolder)) {
                    Files.createDirectories(avatarFolder);
                }

                Path destinationPath = avatarFolder.resolve(imageFile);

                // Xóa file nếu nó đã tồn tại
                if (Files.exists(destinationPath)) {
                    Files.delete(destinationPath);
                }

                // Đọc ảnh gốc từ file
                BufferedImage originalImage = ImageIO.read(selectedFile);

                // Tính toán chiều dài của hình vuông
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                int size = Math.min(width, height);  // Lấy cạnh nhỏ nhất làm kích thước vuông

                // Cắt ảnh thành hình vuông ở giữa
                int x = width;
                int y = height;

                BufferedImage squareImage = originalImage.getSubimage(x, y, size, size);

                // Lưu ảnh vuông vào tệp
                ImageIO.write(squareImage, "PNG", destinationPath.toFile());

                return destinationPath.toAbsolutePath().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        } else {
            System.out.println("Không có ảnh nào được chọn.");
            return null;
        }
    }
    private String getFileExtension(Path path) {
        String fileName = path.toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
    /**
     * hàm này sẽ kiểm tra xem đường dâẫn ảnh có tồn tại trong máy không.
     *
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

    /**
     * Hàm kiểm tra tính hợp lệ của ngày tháng năm.
     *
     * @param dateStr ngày tháng cần kiểm tra
     * @return true/false
     */
    public boolean isValidDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); // Không cho phép ngày không hợp lệ

        try {
            Date date = dateFormat.parse(dateStr); // Kiểm tra định dạng và tính hợp lệ của ngày

            // Kiểm tra ngày không phải là ngày trong tương lai
            if (date.after(new Date())) {
                CustomerAlter.showMessage("Ngày tháng không được là ngày trong tương lai.");
                return false;
            }
        } catch (ParseException e) {
            // Xảy ra ngoại lệ nếu định dạng ngày không hợp lệ
            CustomerAlter.showMessage("Ngày tháng định dạng không hợp lệ phải định dạng dd/MM/yyyy");
            return false;
        }
        return true;
    }


    /**
     * Hàm chuyển đổi format cho ngày tháng năm.
     * Nếu là định dạng dd/MM/yyyy thì chuyển sang yyyy-MM-dd.
     * Nếu là định dạng yyyy-MM-dd thì chuyển sang định dạng dd/MM/yyyy.
     *
     * @param dateStr ngày tháng năm cần chuyển đổi
     * @return ngày tháng năm đã dược chuyển đổi
     */
    public static String reformatDate(String dateStr) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Thử parse theo định dạng dd/MM/yyyy
            try {
                Date date = format1.parse(dateStr);
                return format2.format(date); // Chuyển sang yyyy-MM-dd
            } catch (ParseException e1) {
                // Nếu không được, thử parse theo định dạng yyyy-MM-dd
                Date date = format2.parse(dateStr);
                return format1.format(date); // Chuyển sang dd/MM/yyyy
            }
        } catch (ParseException e2) {
            // Nếu cả hai định dạng đều không đúng
            return null;
        }
    }


    protected static final String DEFAULT_BOOK_IMAGE = "/image/book/default.png";
    protected static Image defaultBookImage;

    protected ObservableList<Category> categoriesList = FXCollections.observableArrayList();

    static {
        defaultBookImage = new Image(BasicController.class.getResource(DEFAULT_BOOK_IMAGE).toExternalForm());

    }


}

