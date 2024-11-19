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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
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

    protected abstract String getRowFXML();

    protected abstract void loadDataFromSource() throws SQLException;

    /**
     * Load lại Data cho Table.
     */
    public void loadData() {
        try {
            itemsList.clear();
            loadDataFromSource();
            loadRows();
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
            itemsList.clear();
            if (isBookType()) {
                itemsList.addAll((Collection<? extends T>) BookDAO.getInstance().searchByCriteria(findCriteria));
            } else if (isMemberType()) {
                itemsList.addAll((Collection<? extends T>) MemberDAO.getInstance().searchByCriteria(findCriteria));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();
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
        loadData();
        setVboxFitWithScrollPane();
    }

    public BasePageController getMainController() {
        return mainController;
    }

    protected abstract void getCriteria();

}
