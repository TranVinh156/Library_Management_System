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

    @FXML
    public void initialize() {
        loadRecentIssuel();
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

    private void loadRecentIssuel() {
        Map<String, Object> findCriteria = new HashMap<>();
        findCriteria.put("status", "PENDING");
        try {
            List<Report> itemsList = ReportDAO.getInstance().searchByCriteria(findCriteria);
            for (Report item : itemsList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/AdminRecentIssuelTableRow.fxml"));
                    HBox row = loader.load();

                    AdminRecentIssueController rowController = loader.getController();
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

    private void loadTopBookHbox() throws SQLException {
        List<Book> highRankBooks;
        highRankBooks = (ArrayList<Book>) ((ArrayList<Book>) BookDAO.getInstance().selectAll()).clone();
        sortBooks(highRankBooks, (book1, book2) -> Integer.compare(book2.getRate(), book1.getRate()));
        try {
            for (Book book : highRankBooks) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/TopBookCard.fxml"));
                HBox row = loader.load();

                AdminDashboardBookCardController rowController = loader.getController();
                rowController.setItem(book);

                childFitWidthParent(row, scrollPane);
                recentIssuelVbox.getChildren().add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sortBooks(List<Book> books, Comparator<Book> comparator) {
        Collections.sort(books, comparator);
    }

}
