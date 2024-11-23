package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.ReportDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Report;
import com.ooops.lms.util.BookManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AdminDashboardController extends BasicController {

    @FXML
    private VBox recentIssuelVbox;

    @FXML
    private HBox topBookHbox;

    @FXML
    private Label totalBookLabel;

    @FXML
    private Label totalBorrowLabel;

    @FXML
    private Label totalIssueLabel;

    @FXML
    private Label totalReaderLabel;

    @FXML
    private ScrollPane scrollPane;

    private AdminBookPageController adminBookPageController;
    private static AdminIssuePageController adminIssuePageController;
    private static AdminMenuController adminMenuController;

    public void setAdminBookPageController(AdminBookPageController adminBookPageController) {
        this.adminBookPageController = adminBookPageController;
    }

    public void setAdminIssuePageController(AdminIssuePageController adminIssuePageController) {
        this.adminIssuePageController = adminIssuePageController;
    }
    public void setAdminMenuController(AdminMenuController adminMenuController) {
        this.adminMenuController = adminMenuController;
        loadRecentIssuel();
        loadTopBookHbox();
    }

    public void setTotalBookLabel(String totalBookLabel) {
        this.totalBookLabel.setText(totalBookLabel);
    }

    public void setTotalBorrowLabel(String totalBorrowLabel) {
        this.totalBorrowLabel.setText(totalBorrowLabel);
    }

    public void setTotalIssueLabel(String totalIssueLabel) {
        this.totalIssueLabel.setText(totalIssueLabel);
    }

    public void setTotalReaderLabel(String totalReaderLabel) {
        this.totalReaderLabel.setText(totalReaderLabel);
    }

    public void loadRecentIssuel() {
        recentIssuelVbox.getChildren().clear();
        Map<String, Object> findCriteria = new HashMap<>();
        findCriteria.put("ReportStatus", "PENDING");
        try {
            List<Report> itemsList = ReportDAO.getInstance().searchByCriteria(findCriteria);
            for (Report item : itemsList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/AdminRecentIssuelTableRow.fxml"));
                    HBox row = loader.load();

                    AdminRecentIssueController rowController = loader.getController();
                    if(adminIssuePageController == null) {
                        System.out.println("adminIssuePageController is null");
                    }
                    rowController.setMenuController(this.adminMenuController);
                    rowController.setMainController(adminIssuePageController);
                    rowController.setItem(item);

                    childFitWidthParent(row, scrollPane);
                    recentIssuelVbox.getChildren().add(row);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTopBookHbox() {
        topBookHbox.getChildren().clear();
        try {
             List<Book> highRankBooks = BookDAO.getInstance().selectAll();

            sortBooks(highRankBooks, (book1, book2) -> Integer.compare(book2.getRate(), book1.getRate()));
            int count =0;
            for (Book book : highRankBooks) {
                count++;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/TopBookCard.fxml"));
                VBox row = loader.load();

                AdminDashboardBookCardController rowController = loader.getController();
                rowController.setAdminMenuController(adminMenuController);
                rowController.setMainController(adminBookPageController);
                rowController.setItem(book);

                topBookHbox.getChildren().add(row);
                if(count == 11) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortBooks(List<Book> books, Comparator<Book> comparator) {
        Collections.sort(books, comparator);
    }

}
