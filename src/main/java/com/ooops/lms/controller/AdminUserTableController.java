package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.*;

public class AdminUserTableController extends BaseTableController<Member, AdminUserPageController, AdminUserTableRowController> {
    protected static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminUserTableRow.fxml";
    @FXML
    private Button addButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchText;

    @FXML
    private VBox tableVbox; /* đổi tên thành TableVbox*/

    private String findValue;

    private AdminDashboardController adminDashboardController;

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(MemberDAO.getInstance().selectAll());
        adminDashboardController.setTotalReaderLabel(itemsList.size()+"");
    }

    @FXML
    public void initialize() {
        adminDashboardController = dashboardLoader.getController();
        // Lắng nghe sự thay đổi trong TextField tìm kiếm
        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.trim().isEmpty()) {
                    findValue = newValue;
                    searchCriteria();
                } else {
                    // Nếu trường tìm kiếm rỗng, tải lại toàn bộ dữ liệu
                    loadData();
                }
            }
        });
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    @Override
    protected void getCriteria() {
    }
    @Override
    protected void searchCriteria() {
        itemsList.clear();
        try {
            findCriteria.clear();
            findCriteria.put("phone", findValue);
            itemsList.addAll(MemberDAO.getInstance().searchByCriteria(findCriteria));

            findCriteria.clear();
            findCriteria.put("member_id", findValue);
            itemsList.addAll(MemberDAO.getInstance().searchByCriteria(findCriteria));

            findCriteria.clear();
            findCriteria.put("fullname", findValue);
            itemsList.addAll(MemberDAO.getInstance().searchByCriteria(findCriteria));


        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadRows();
    }


}
