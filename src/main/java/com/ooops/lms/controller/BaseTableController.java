package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Member;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseTableController<T, P extends BasePageController, R extends BaseRowController<T, P>> extends BasicController {
    @FXML
    protected ScrollPane scrollPane;
    @FXML
    protected VBox tableVbox;

    protected ObservableSet<T> itemsList = FXCollections.observableSet(new HashSet<>());
    protected BasePageController mainController;
    protected Map<String, Object> findCriteria = new HashMap<>();

    private static final int LOAD_THRESHOLD = 5;  // Số hàng trước khi đến cuối bảng để kích hoạt tải thêm
    private int loadedItemsCount = 0;  // Theo dõi số hàng hiện tại đã được tải
    private static final int ITEMS_PER_LOAD = 10; // Số hàng sẽ được tải mỗi lần

    protected abstract String getRowFXML();

    protected abstract void loadDataFromSource() throws SQLException;

    /**
     * Load lại Data cho Table.
     */
    public void loadData() {
        try {
            itemsList.clear();
            loadDataFromSource();
            loadedItemsCount = 0;
            tableVbox.getChildren().clear();
            loadMoreRows();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load lại các row.
     */
    protected void loadRows() {
        tableVbox.getChildren().clear();

        for (T item : itemsList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(getRowFXML()));
                HBox row = loader.load();

                BaseRowController rowController = loader.getController();
                rowController.setMainController(this.getMainController());
                rowController.setItem(item);

                childFitWidthParent(row, scrollPane);
                tableVbox.getChildren().add(row);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected void searchCriteria() {
        getCriteria();
        try {
            //itemsList.clear();
            //itemsList.addAll((Collection<? extends T>) BookDAO.getInstance().searchByCriteria(findCriteria));
            if (isBookType()) {
                itemsList.addAll((Collection<? extends T>) BookDAO.getInstance().searchByCriteria(findCriteria));
            } else if (isMemberType()) {
                itemsList.addAll((Collection<? extends T>) MemberDAO.getInstance().searchByCriteria(findCriteria));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(itemsList.size());
        loadRows();
    }

    private boolean isBookType() {
        try {
            // Get the actual type parameter using reflection
            Class<?> typeClass = getTypeParameterClass();
            return Book.class.isAssignableFrom(typeClass);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMemberType() {
        try {
            Class<?> typeClass = getTypeParameterClass();
            return Member.class.isAssignableFrom(typeClass);
        } catch (Exception e) {
            return false;
        }
    }

    private Class<T> getTypeParameterClass() {
        // This is a simple implementation. You might want to make it more robust
        try {
            String className = getClass().getGenericSuperclass().getTypeName();
            String typeParam = className.substring(className.indexOf('<') + 1, className.indexOf(','));
            return (Class<T>) Class.forName(typeParam);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected void setVboxFitWithScrollPane() {
        childFitWidthParent(tableVbox, scrollPane);
        childFitHeightParent(tableVbox, scrollPane);
    }

    public void setMainController(BasePageController mainController) {
        this.mainController = mainController;
        setScrollListener();
        loadData();
        setVboxFitWithScrollPane();
    }

    public BasePageController getMainController() {
        return mainController;
    }

    protected abstract void getCriteria();

    private void setScrollListener() {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                // Kiểm tra xem người dùng có gần đến cuối bảng không
                if (scrollPane.getContent().getBoundsInLocal().getMaxY() - scrollPane.getVvalue() * scrollPane.getContent().getBoundsInLocal().getHeight() <= LOAD_THRESHOLD) {
                    // Nếu gần đến cuối, tải thêm hàng
                    loadMoreRows();
                }
            }
        });
    }

    protected void loadMoreRows() {
        if (loadedItemsCount < itemsList.size()) {
            int remainingItems = itemsList.size() - loadedItemsCount;
            int itemsToLoad = Math.min(ITEMS_PER_LOAD, remainingItems);

            // Tải thêm nhóm hàng tiếp theo
            for (int i = 0; i < itemsToLoad; i++) {
                if (loadedItemsCount < itemsList.size()) {
                    T item = (T) itemsList.toArray()[loadedItemsCount];  // Lấy mục tiếp theo từ danh sách
                    loadRow(item);
                    loadedItemsCount++;
                }
            }
        }
    }

    protected void loadRow(T item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getRowFXML()));
            HBox row = loader.load();
            BaseRowController rowController = loader.getController();
            rowController.setMainController(this.getMainController());
            rowController.setItem(item);
            childFitWidthParent(row, scrollPane);
            tableVbox.getChildren().add(row);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

