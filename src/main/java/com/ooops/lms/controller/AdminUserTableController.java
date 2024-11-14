package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Gender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AdminUserTableController extends BasicUserController {
    @FXML
    private Button addButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchText;

    @FXML
    private VBox userTableVbox;

    private ObservableList<Member> membersList = FXCollections.observableArrayList();
    private AdminUserPageController mainController;

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

    /**
     * Hàm để set mainController (PageController).
     * Sau khi có mainController thì mới được tải hàng Row.
     * Để tránh cho set mainController cho các Row bị rỗng.
     *
     * @param controller là PageController truyền vào
     */
    public void setMainController(AdminUserPageController controller) throws SQLException {
        this.mainController = controller;
        loadData();
        setVboxFitWithScrollPane();
    }

    public void loadFindData(String name) {
        try {
            membersList.clear();
            // Sử dụng Map với key là ID của Member
            Map<Integer, Member> uniqueMembersMap = new HashMap<>();

            // Tìm kiếm theo phone
            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("phone", name);
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
            membersList.addAll(uniqueMembersMap.values());
            loadRows();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Hàm load lại Data cho Table.
     */
    public void loadData() {
        try {
            membersList.clear();
            membersList.addAll(MemberDAO.getInstance().selectAll());
            loadRows();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Load lại các row.
     * Lấy các member ra từ memberList và tạo row cho member đẩy.
     * Sau đó đẩy các row vào bảng.
     */
    public void loadRows() {
        //Xóa hết các row cũ trong bảng
        userTableVbox.getChildren().clear();

        //Tạo các row cho mỗi member và đẩy vào bảng
        for (Member member : membersList) {
            try {
                //Tạp row cho member
                FXMLLoader loader = new FXMLLoader(getClass().getResource(USER_TABLE_ROW_FXML));
                HBox row = loader.load();

                //Xử lý gán mainController(PageController) cho Row
                AdminUserTableRowController rowController = loader.getController();
                rowController.setMainController(mainController);

                //Gán member của row
                rowController.setMember(member);

                //Làm cho row chiều ngang resize theo Table
                childFitWidthParent(row, scrollPane);
                //Đẩy row vào Table
                userTableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace(); // Bắt các lỗi khác
            }
        }
    }

    /**
     * Làm cho Vbox nằm trong ScrollPane resize theo ScrollPane
     */
    private void setVboxFitWithScrollPane() {
        childFitWidthParent(userTableVbox, scrollPane);
        childFitHeightParent(userTableVbox, scrollPane);
    }

}
