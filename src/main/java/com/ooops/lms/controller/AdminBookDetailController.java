package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.SuggestionTable.SuggestionRowClickListener;
import com.ooops.lms.SuggestionTable.SuggestionTable;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Category;
import com.ooops.lms.model.Member;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminBookDetailController extends BasicBookController {


    @FXML
    private TextField ISBNText;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane addButtonPane;

    @FXML
    private TextField authorNameText;

    @FXML
    private TextArea bookContentText;

    @FXML
    private ImageView bookImage;

    @FXML
    private TextField bookNameText;

    @FXML
    private TextField categoryText;

    @FXML
    private Button choiceImageButton;

    @FXML
    private AnchorPane copyBookPane;

    @FXML
    private VBox copyBookTableVbox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button ediButton;

    @FXML
    private TextField locationText;

    @FXML
    private HBox mainPane;

    @FXML
    private TextField numberOfBookText;

    @FXML
    private TextField numberOfBorrowText;

    @FXML
    private TextField numberOfLostText;

    @FXML
    private Button page1Button;

    @FXML
    private Button page2Button;

    @FXML
    private Button saveButton;

    @FXML
    private Button scanButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ScrollPane suggestionPane;
    @FXML
    private VBox suggestionVbox;

    private AdminBookPageController mainController;
    private SuggestionTable suggestionTable;
    private Book book;

    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean isPage1 = true;
    private boolean isSettingItem = false;
    private ObservableList<BookItem> bookItemList = FXCollections.observableArrayList();

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        suggestionTable = new SuggestionTable(this.suggestionPane, this.suggestionVbox);
        suggestionTable.setRowClickListener(new SuggestionRowClickListener() {
            @Override
            public void onRowClick(Object o) {
                if(o instanceof Book) {
                    isSettingItem = true;
                    setItem((Book)o);
                    suggestionVbox.getChildren().clear();
                    suggestionPane.setVisible(false);
                }
            }
        });

        childFitWidthParent(copyBookTableVbox, scrollPane);

        // Listener cho ISBN TextField
        ISBNText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!isSettingItem && addMode) {
                suggestionTable.updateSuggestionPanePosition(ISBNText);
                // Reset và restart pause transition mỗi khi có thay đổi text
                pauseTransition.setOnFinished(event -> {
                    String isbnNumbers = newValue.replaceAll("[^0-9]", "");
                    suggestionTable.loadFindData("bookAPI", isbnNumbers);
                });
                pauseTransition.playFromStart();
            }
            isSettingItem = false;
        });

        bookNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!isSettingItem && addMode) {
                suggestionTable.updateSuggestionPanePosition(bookNameText);
                // Reset và restart pause transition mỗi khi có thay đổi text
                pauseTransition.setOnFinished(event -> {
                    suggestionTable.loadFindData("bookAPI", newValue);
                });
                pauseTransition.playFromStart();
            }
        });
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        if (getNewBookInformation()) {
            boolean confirmYes = CustomerAlter.showAlter("Thêm sách mới?");
            if (confirmYes) {
                //Thêm sách trong CSDL
                Command addCommand = new AdminCommand("add", this.book);
                commandInvoker.setCommand(addCommand);
                if (commandInvoker.executeCommand()) {
                    mainController.loadData();
                    setAddMode(false);
                    System.out.println("Đã lưu thay đổi");
                }
            }
        }
    }

    @FXML
    public void onChoiceImageButtonAction(ActionEvent event) {
        book.setImagePath(getImagePath());

        //Nếu như có chọn ảnh thì set ảnh cho bookImage
        if (book.getImagePath() != null) {
            Image image = new Image(book.getImagePath());
            bookImage.setImage(image);
        }
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn muốn xóa quyển sách này?");
        if (confrimYes) {
            //Xóa sách trong CSDL
            Command deleteCommand = new AdminCommand("delete", this.book);
            commandInvoker.setCommand(deleteCommand);
            if (commandInvoker.executeCommand()) {
                mainController.loadData();
                mainController.alterPage();
                loadStartStatus();
                System.out.println("Đã xóa sách");
            } else {
                CustomerAlter.showMessage("Xóa sách thất bại");
            }
        } else {
            System.out.println("Tiếp tục edit");
        }

    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (book != null) {
            setEditMode(true);
        }
    }

    @FXML
    void onPage1ButtonAction(ActionEvent event) {
        isPage1 = true;
        setButtonPageAnimation();

        //Chuyển sang Page1
        copyBookPane.setVisible(!isPage1);
    }

    @FXML
    void onPage2ButtonAction(ActionEvent event) {
        isPage1 = false;
        setButtonPageAnimation();

        //Chuyển sang Page2
        copyBookPane.setVisible(!isPage1);
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
        if (getNewBookInformation()) {
            if (confirmYes) {
                // sửa sách trong CSDL
                Command editCommand = new AdminCommand("edit", this.book);
                commandInvoker.setCommand(editCommand);
                if (commandInvoker.executeCommand()) {
                    mainController.loadData();
                    setEditMode(false);
                    System.out.println("Đã lưu thay đổi");
                }

            }
        } else {
            System.out.println("Tiếp tục edit");
        }
    }

    @FXML
    void onScanButtonAction(ActionEvent event) {

    }

    /**
     * Khi thêm Book mới thì sẽ lấy tất cả thông tin của các trường lưu vào book của Controller này.
     * Kết hợp với hàm kiểm tra thông tin để biết người dùng đã nhập các thông tin hợp lệ hay chưa.
     *
     * @return true/false
     */
    private boolean getNewBookInformation() {
        //Xử lý ISBN
        String isbnText = ISBNText.getText().replaceAll("[^0-9]", "");
        book.setISBN(Long.parseLong(isbnText));
        //Xử lý các thông tin khác
        book.setTitle(bookNameText.getText());
        book.setPlaceAt(locationText.getText());
        book.setQuantity(Integer.parseInt(numberOfBookText.getText()));
        book.setDescription(bookContentText.getText());

        //Kiểm tra thông tin người dùng nhập hợp lệ hay không
        if (checkInformation(book)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Hàm kiểm tra thông tin người dùng nhập.
     *
     * @param book là book cần kiểm tra
     * @return
     */
    private boolean checkInformation(Book book) {
        //Kiểm tra ISBN và in thông báo lỗi
        String isbnText = String.valueOf(book.getISBN());
        try {
            // Parse ISBN only if it's a valid long number
            if (!isbnText.isEmpty()) {
                Long isbn = Long.parseLong(isbnText);
                book.setISBN(isbn);
            } else {
                CustomerAlter.showMessage("ISBN cannot be empty.");
                return false;
            }
        } catch (NumberFormatException e) {
            CustomerAlter.showMessage("Please enter a valid numeric ISBN.");
            return false;
        }

        //Kiểm tra tên sách và báo lỗi nếu có
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            CustomerAlter.showMessage("Không được để tên sách trống");
            return false;
        }

        //Kiểm tra nơi đặt sách và báo lỗi nếu có
        if (book.getPlaceAt() == null || book.getPlaceAt().trim().isEmpty()) {
            CustomerAlter.showMessage("Không được để vị trí sách trống");
            return false;
        }

        return true;
    }

    /**
     * Load các bookItem của book vào bảng ở Page2.
     */
    private void loadBookItemData() {
        bookItemList.clear();
        copyBookTableVbox.getChildren().clear();

        //Lấy dữ liệu
        try {
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("ISBN", String.valueOf(book.getISBN()));
            bookItemList.addAll(BookItemDAO.getInstance().searchByCriteria(criteria));
        } catch (Exception e) {
            System.out.println("Lỗi bookItemList addAll:" + e.getMessage());
        }

        //Đẩy các Row vào bảng
        for (BookItem bookItem : bookItemList) {
            try {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_COPY_ROW_FXML));
                    HBox row = loader.load();

                    AdminBookCopyRowController rowController = loader.getController();
                    rowController.setMainController(mainController);
                    rowController.setBookItem(bookItem);
                    childFitWidthParent(row, rowController);
                    copyBookTableVbox.getChildren().add(row);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hàm dùng để lấy thông tin từ book và gán thông tin cho các trường.
     *
     * @param book
     */
    public void setItem(Book book) {
        this.book = book;
        bookNameText.setText(book.getTitle());
        ISBNText.setText(String.valueOf(book.getISBN()));
        authorNameText.setText(getAuthors(book.getAuthors()));
        categoryText.setText(getCategories(book.getCategories()));
        numberOfBookText.setText(book.getQuantity() + "");
        numberOfBorrowText.setText(String.valueOf(book.getNumberOfLoanedBooks()));
        numberOfLostText.setText(String.valueOf(book.getNumberOfLostBooks()));
        locationText.setText(book.getPlaceAt());
        bookContentText.setText(book.getDescription());

        if (book.getImagePath() != null && isValidImagePath(book.getImagePath())) {
            bookImage.setImage(new Image(book.getImagePath()));
        } else {
            bookImage.setImage(defaultBookImage);
        }

        loadBookItemData();
    }

    /**
     * Set edit Mode.
     *
     * @param edit
     */
    public void setEditMode(boolean edit) {
        editMode = edit;
        //Xử lý ẩn hiện các nút bấm
        ediButton.setVisible(!edit);
        addButtonPane.setVisible(!edit);
        deleteButton.setVisible(edit);
        saveButton.setVisible(edit);
        choiceImageButton.setVisible(edit);

        //cho phép các trường có thể sửa đổi
        ISBNText.setEditable(edit);
        bookNameText.setEditable(edit);
        locationText.setEditable(edit);
        authorNameText.setEditable(edit);
        bookContentText.setEditable(edit);
        categoryText.setEditable(edit);
        numberOfBookText.setEditable(edit);
    }

    /**
     * Set Add Mode.
     *
     * @param add
     */
    public void setAddMode(boolean add) {
        addMode = add;
        //Xử lý các nút bấm
        ediButton.setVisible(!add);
        addButtonPane.setVisible(add);
        addButton.setVisible(add);
        choiceImageButton.setVisible(add);
        scanButton.setVisible(add);

        //Cho phép các trường chỉnh sửa
        ISBNText.setEditable(add);
        bookNameText.setEditable(add);
        locationText.setEditable(add);
        authorNameText.setEditable(add);
        categoryText.setEditable(add);
        numberOfBookText.setEditable(add);

        //Nếu như mà là mở addMode thì các trường thông tin set về rỗng
        if (addMode) {
            deleteButton.setVisible(!add);
            saveButton.setVisible(!add);
            ISBNText.setText(null);
            bookNameText.setText(null);
            locationText.setText(null);
            authorNameText.setText(null);
            categoryText.setText(null);
            numberOfBookText.setText(null);
            numberOfBorrowText.setText(null);
            numberOfLostText.setText(null);
            bookContentText.setText(null);
            bookImage.setImage(defaultBookImage);
        }
    }

    /**
     * Hàm để load bảng Detail về trạng thái rỗng ban đầu.
     */
    public void loadStartStatus() {
        book = new Book();
        setAddMode(false);
        setEditMode(false);
    }

    /**
     * Hàm xử lý Animation(popUp) cho nút bấm Page1 và Page2.
     */
    private void setButtonPageAnimation() {
        if (isPage1) {
            //choice button color darker
            page1Button.setStyle("-fx-background-color: #DDDCDC;");
            page2Button.setStyle("-fx-background-color: #FFF;");

            //choice button bring to front
            if (page1Button.getParent().getChildrenUnmodifiable().indexOf(page1Button) <
                    page1Button.getParent().getChildrenUnmodifiable().size() - 1) {
                page2Button.toBack();
            }
        } else {
            //choice button color darker
            page2Button.setStyle("-fx-background-color: #DDDCDC;");
            page1Button.setStyle("-fx-background-color: #FFF;");

            //choice button bring to front
            if (page2Button.getParent().getChildrenUnmodifiable().indexOf(page2Button) <
                    page2Button.getParent().getChildrenUnmodifiable().indexOf(page1Button)) {
                page1Button.toBack();
            }
        }
    }


}
