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

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(MemberDAO.getInstance().selectAll());
    }
    @Override
    protected void getCriteria(){

    }

    @FXML
    public void initialize() {
        // Lắng nghe sự thay đổi trong TextField tìm kiếm
        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.trim().isEmpty()) {
                    // Khi người dùng nhập vào, lọc lại dữ liệu và hiển thị kết quả
                    loadFindData(newValue);
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

    public void loadFindData(String name) {
        try {
            itemsList.clear();
            // Sử dụng Map với key là ID của Member
            Map<Integer, Member> uniqueMembersMap = new HashMap<>();

            // Tìm kiếm theo phone
            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("phone", name);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }

            searchCriteria.put("member_id", name);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }

            // Tìm kiếm theo first_name
            searchCriteria.clear();
            searchCriteria.put("first_name", name);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }

            // Tìm kiếm theo last_name
            searchCriteria.clear();
            searchCriteria.put("last_name", name);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }

            // Thêm tất cả các giá trị unique vào membersList
            itemsList.addAll(uniqueMembersMap.values());
            loadRows();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
