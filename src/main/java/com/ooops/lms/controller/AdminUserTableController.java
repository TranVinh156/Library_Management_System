package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Gender;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUserTableController extends BasicUserController {
    @FXML
    private Button addButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchText;

    @FXML
    private VBox userTableVbox;

    private MemberDAO memberDAO = new MemberDAO();
    private ObservableList<Member> membersList = FXCollections.observableArrayList();
    private AdminUserPageController mainController;

    @FXML
    public void initialize() {

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    /**
     * Hàm để set mainController (PageController).
     * Sau khi có mainController thì mới được tải hàng Row.
     * Để tránh cho set mainController cho các Row bị rỗng.
     * @param controller là PageController truyền vào
     */
    public void setMainController(AdminUserPageController controller) throws SQLException {
        this.mainController = controller;
        loadData();
        setVboxFitWithScrollPane();
    }

    /**
     * Hàm load lại Data cho Table.
     */
    public void loadData() {
        try {
            membersList.clear();
            membersList.addAll(memberDAO.selectAll());
            loadRows();
        } catch (SQLException e) {
            e.printStackTrace();
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
