package com.ooops.lms.controller;

import com.ooops.lms.Command.UserCommand;
import com.ooops.lms.animation.Animation;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AdvancedSearchController {
    @FXML
    private Pagination pagination;
    @FXML
    private TextField searchText;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    HBox categoryBox;

    private ScrollPane scrollPane1 = new ScrollPane(), scrollPane2 = new ScrollPane();
    private Map<String, Object> criteria = new HashMap<>();

    private static final String DASHBOARD_FXML = "/com/ooops/lms/library_management_system/DashBoard-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();
    private List<Book> findBook;
    private VBox test = new VBox();
    private HBox row1Box = new HBox(), row2Box = new HBox();

    public void initialize() {
        categoryChoiceBox.getItems().addAll("title", "category_name", "author_name");
        categoryChoiceBox.setValue("title");
        scrollPane1.getStyleClass().add("real-transparent-scrollpane");
        scrollPane2.getStyleClass().add("real-transparent-scrollpane");

        scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            List<Category> categoryList = BookDAO.getInstance().selectAllCategory();
            for (int i = 0; i < categoryList.size(); i++) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/ooops/lms/library_management_system/Category-view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    CategoryController categoryController = fxmlLoader.getController();
                    categoryController.setData(categoryList.get(i).getCatagoryName(),this);
                    categoryBox.getChildren().add(anchorPane);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Sound.getInstance().playSound("cacBanYeuOi.mp3");
        Animation.getInstance().showMessage("Bạn yêu ơi, vì 1 sách có 1 thể thoi nên nó sẽ tìm thể loại cuối bạn chọn nhé! Mình lười code chỉ được chọn 1 cho bạn yêu quá");
    }

    public void setSearchText(String keyword) {
        searchText.setText(keyword);
        onSearchButtonAction(new ActionEvent());
    }

    public void addCategoryCriteria(String category) {
        criteria.put("category_name",category);
    }

    public void deleteCategoryCriteria(String category) {
        Iterator<Map.Entry<String, Object>> iterator = criteria.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == category) {
                iterator.remove();
                break;
            }
        }
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

        criteria.put(category, keyword);
        UserCommand searchCommand = new UserCommand("searchBookByCategory", criteria);

        if (searchCommand.execute()) {
            findBook = (List<Book>) searchCommand.getObject();
            System.out.println("Tìm thấy " + findBook.size() + " sách");
        } else {
            System.out.println("Không thấy sách");
        }
        deleteCategoryCriteria(searchText.getText());
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