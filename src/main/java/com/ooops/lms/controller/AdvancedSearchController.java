package com.ooops.lms.controller;

import com.ooops.lms.Command.UserCommand;
import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchController {
    @FXML
    private Pagination pagination;
    @FXML
    private TextField searchText;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private ScrollPane scrollPane1=new ScrollPane(),scrollPane2=new ScrollPane();

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private List<Book> findBook;
    private VBox test = new VBox();
    private HBox row1Box = new HBox(), row2Box = new HBox();

    public void initialize() {
        categoryChoiceBox.getItems().addAll("title", "category_name", "author_name");
        categoryChoiceBox.setValue("title");
        scrollPane1.setStyle("-fx-background-color: transparent;");
        scrollPane2.setStyle("-fx-background-color: transparent;");

        scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

    /**
     * quay lại
     *
     * @param event ấn vao
     */
    public void onBackButtonAction(ActionEvent event) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(DASHBOARD_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    /**
     * tìm sách
     *
     * @param event tim
     */
    public void onSearchButtonAction(ActionEvent event) {
        String keyword = searchText.getText();
        String category = categoryChoiceBox.getValue();

        if (category == null || keyword.isEmpty()) {
            UserCommand searchCommand = new UserCommand("getAllBooks", null);
        }

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(category, keyword);
        UserCommand searchCommand = new UserCommand("searchBookByCategory", criteria);

        if (searchCommand.execute()) {
            findBook = (List<Book>) searchCommand.getObject();
            System.out.println("Tìm thấy " + findBook.size() + " sách");
        } else {
            System.out.println("Không thấy sách");
        }

        showFindedBook();
    }

    /**
     * tìm xong thì hiển thị ra
     */
    private void showFindedBook() {
        int numberOfPage = (findBook.size() - 1) / 12 + 1;
        pagination.setPageCount(numberOfPage);
        pagination.setPageFactory(pageIndex -> loadBook(pageIndex * 12, Math.min((pageIndex + 1) * 12, findBook.size())));
    }

    /**
     * load lên các HBox cho đẹp hàng.
     *
     * @param start index bắt đầu
     * @param end   index kết thúc
     * @return vbox đã load sách sau khi tìm
     */
    private VBox loadBook(int start, int end) {
        row1Box.getChildren().clear();
        row2Box.getChildren().clear();
        test.getChildren().clear();

        for (int i = start; i < Math.min(start + 6, end); i++) {
            loadBookCard(i, row1Box);
        }
        for (int i = start + 6; i < Math.min(start + 12, end); i++) {
            loadBookCard(i, row2Box);
        }
        scrollPane1.setContent(row1Box);
        scrollPane2.setContent(row2Box);
        test.getChildren().addAll(scrollPane1, scrollPane2);
        test.setAlignment(Pos.CENTER);
        return test;
    }

    private void loadBookCard(int index, HBox rowBox) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/BookCard2-view.fxml"));
            VBox cardBox = fxmlLoader.load();
            BookCard2Controller cardController = fxmlLoader.getController();
            cardController.setData(findBook.get(index));
            rowBox.getChildren().add(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}