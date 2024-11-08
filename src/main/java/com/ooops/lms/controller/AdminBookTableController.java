package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class AdminBookTableController extends BasicBookController {

    @FXML
    private TextField ISBNFindText;

    @FXML
    private Button addButton;

    @FXML
    private TextField authorFindText;

    @FXML
    private TextField bookNameFindTExt;

    @FXML
    private VBox bookTableVbox;

    @FXML
    private Button categoryFindButton;

    @FXML
    private VBox categoryList;

    @FXML
    private AnchorPane categoryTable;

    @FXML
    private Button findButton;

    @FXML
    private HBox mainPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField stausText;

    @FXML
    private AnchorPane tableBookPane;

    @FXML
    private Label totalNumberBookLabel;

    @FXML
    private Label totalNumberBorrowLabel;

    @FXML
    private Label totalNumberIssueLabel;

    @FXML
    private Label totalNumberLostLabel;

    private Book book;
    private BookDAO bookDAO;

    private ObservableList<Book> bookList;
    private AdminBookPageController mainController;

    public void registerNewItem(Book book) {
        addBook(book);
    }
    private void addBook(Book book) {
        bookList.add(book);
    }

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
        loadData();
    }

    @FXML
    public void initialize() {
        bookList = FXCollections.observableArrayList();

        // Tạo danh sách tác giả cho các quyển sách
        List<Author> authors1 = Arrays.asList(
                new Author(1, "John Doe"),
                new Author(2, "Jane Smith")
        );
        List<Author> authors2 = Arrays.asList(
                new Author(3, "Alice Johnson"),
                new Author(4, "Bob Brown")
        );
        List<Author> authors3 = Arrays.asList(
                new Author(5, "Chris White")
        );

        // Tạo danh sách thể loại cho các quyển sách
        List<Category> categories1 = Arrays.asList(
                new Category(1, "Programming"),
                new Category(2, "Technology")
        );
        List<Category> categories2 = Arrays.asList(
                new Category(3, "Science Fiction"),
                new Category(4, "Adventure")
        );
        List<Category> categories3 = Arrays.asList(
                new Category(5, "History"),
                new Category(6, "Biography")
        );

        setCategoryList();

        setVboxFitWithScrollPane();

        bookDAO = new BookDAO();
    }

    private void loadData() {
        bookTableVbox.getChildren().clear();
        for(Book book: bookList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_TABLE_ROW_FMXL));
                HBox row = loader.load();

                AdminBookTableRowController rowController = loader.getController();
                rowController.setMainController(mainController);
                rowController.setBook(book);
                childFitWidthParent(row,rowController);
                bookTableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVboxFitWithScrollPane() {
        childFitWidthParent(bookTableVbox,scrollPane);
        childFitHeightParent(bookTableVbox,scrollPane);
    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        updateCategoryTablePosition();
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

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {

    }

    private void setCategoryList() {
        // Đặt sự kiện chuột cho vùng chứa chính
        mainPane.setOnMouseClicked(event -> {
            // Kiểm tra xem có nhấn vào danh sách danh mục không
            if (categoryTable.isVisible() && !categoryList.getBoundsInLocal().contains(event.getX(), event.getY())) {
                categoryTable.setVisible(false); // Ẩn danh mục nếu bấm ngoài
            }
        });

        // Thêm listener để điều chỉnh vị trí của categoryTable theo categoryFindButton
        categoryFindButton.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            updateCategoryTablePosition();
        });

        // Tạo danh sách danh mục từ một nguồn dữ liệu
        List<String> categories = List.of("Danh Mục 1", "Danh Mục 2", "Danh Mục 3");
        for (String category : categories) {
            CheckBox categoryCheckBox = new CheckBox(category);
            categoryCheckBox.setOnAction(this::onCategorySelected); // Gọi sự kiện khi nhấn checkbox
            categoryList.getChildren().add(categoryCheckBox);
        }
    }

    private void updateCategoryTablePosition() {
        // Lấy tọa độ X,Y của categoryFindButton trong hệ tọa độ scene
        Point2D buttonCoords = categoryFindButton.localToScene(0, 0);
        double buttonSceneX = buttonCoords.getX(); // tọa độ X của button trong không gian cảnh
        double buttonSceneY = buttonCoords.getY(); // tọa độ Y của button trong không gian cảnh

        // Lấy tọa độ X,Y của mainPane trong hệ tọa độ scene
        Point2D mainPaneCoords = mainPane.localToScene(0, 0);
        double mainPaneSceneX = mainPaneCoords.getX(); // tọa độ X của mainPane trong không gian cảnh
        double mainPaneSceneY = mainPaneCoords.getY(); // tọa độ Y của mainPane trong không gian cảnh

        // Tính toán tọa độ X của categoryFindButton so với mainPane
        double buttonRelativeX = buttonSceneX - mainPaneSceneX;
        double buttonRelativeY = buttonSceneY - mainPaneSceneY; // nếu cần tính tọa độ Y, có thể thêm

        // In ra tọa độ X và Y tương đối
        System.out.println("Tọa độ X của categoryFindButton so với mainPane: " + buttonRelativeX);
        System.out.println("Tọa độ Y của categoryFindButton so với mainPane: " + buttonRelativeY);

        // Cập nhật vị trí cho categoryTable (nếu cần)
        categoryTable.setLayoutX(buttonRelativeX);
        // Nếu cần cập nhật vị trí Y, có thể thêm:
        categoryTable.setLayoutY(buttonRelativeY+33);
    }



}
