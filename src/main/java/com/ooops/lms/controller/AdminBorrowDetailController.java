package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.SuggestionTable.SuggestionRowClickListener;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.*;
import com.ooops.lms.SuggestionTable.SuggestionTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdminBorrowDetailController extends BasicBorrowController {

    @FXML
    private Button addButotn;

    @FXML
    private TextField authorNameText;

    @FXML
    private TextField barCodeText;

    @FXML
    private TextField bookNameText;

    @FXML
    private TextField borowDateText;

    @FXML
    private TextField categoryText;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField emailText;

    @FXML
    private TextField genderText;

    @FXML
    private TextField memberIDText;

    @FXML
    private TextField memberNameText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private TextField returnDateText;

    @FXML
    private Button saveButton;

    @FXML
    private AnchorPane savePane;

    @FXML
    private Button scanBookButton;

    @FXML
    private Button scanMemberButton;

    @FXML
    private TextField statusText;

    @FXML
    private ScrollPane suggestionPane;

    @FXML
    private TextField totalOFBorrowText;

    @FXML
    private TextField totalOfLostText;

    @FXML
    private VBox suggestionVbox;

    private AdminBorrowPageController mainController;
    private boolean addMode;
    private Member member;
    private BookItem bookItem;
    private BookReservation bookReservation;
    private ObservableList<Member> membersSuggestList = FXCollections.observableArrayList();
    private ObservableList<Object> suggestList = FXCollections.observableArrayList();
    private SuggestionTable suggestionTable;

    @FXML
    public void initialize() {
        suggestionTable = new SuggestionTable(this.suggestionVbox);
        // Đăng ký listener để xử lý sự kiện click
        suggestionTable.setRowClickListener(new SuggestionRowClickListener() {
            @Override
            public void onRowClick(Object o) {
                if(o instanceof Member) {
                    setMember((Member)o);
                    suggestionVbox.getChildren().clear();
                    suggestionPane.setVisible(false);
                } else if(o instanceof BookItem) {
                    setBookItem((BookItem)o);
                }
            }
        });

        // Lắng nghe sự thay đổi trong TextField tìm kiếm
        memberIDText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               // if (newValue != null && !newValue.isEmpty()) {
                    loadMemberSuggestionFindData(newValue);

            }
        });
        barCodeText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.isEmpty()) {
                    loadBookFindData(newValue);
                }
            }
        });

        memberNameText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadMemberSuggestionFindData(newValue);
            }
        });
    }

    private void loadMemberSuggestionFindData(String newValue) {
        if(newValue == null || newValue.isEmpty()) {
            suggestionVbox.getChildren().clear();
            suggestionPane.setVisible(false);
            return;
        }
        try {
            suggestList.clear();
            suggestionVbox.getChildren().clear();
            Map<Integer, Member> uniqueMembersMap = new HashMap<>();
            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.clear();
            // Tìm kiếm theo last_name
            searchCriteria.clear();
            searchCriteria.put("first_name", newValue);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }
            searchCriteria.clear();
            searchCriteria.put("last_name", newValue);
            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                uniqueMembersMap.put(member.getPerson().getId(), member);
            }
            suggestList.addAll(uniqueMembersMap.values());
            if(suggestList.size() > 0) {
                suggestionPane.setVisible(true);
                suggestionTable.loadSuggestionRows(suggestList);
            } else {
                suggestionPane.setVisible(false);
            }

        } catch (Exception e) {
            System.out.println("Lỗi loadMemberSuggestion: "+e.getMessage());
        }
    }

    void setMainController(AdminBorrowPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void oSaveButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
        if (confrimYes) {
            setEditMode(false);
        }
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn thêm người này?");
        if (confirmYes) {
            // Command addCommand = new AdminCommand("add",);
            // commandInvoker.setCommand(addCommand);
            //Thang cho Vinh
        }
        setAddMode(false);
    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn xóa người này?");
        if (confirmYes) {
            setEditMode(false);
        }
    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        setEditMode(true);
    }

    @FXML
    void onScanButtonAction(ActionEvent event) {

    }

    private void loadMemberFindData(String newValue) {
        // Khi người dùng nhập vào, lọc lại dữ liệu và hiển thị kết quả
        member = new Member(null, null, null);
        member.setAccountId(Integer.parseInt(newValue));

        Command findMemberCommand = new AdminCommand("find", member);
        commandInvoker.setCommand(findMemberCommand);
        if (commandInvoker.executeCommand()) {
            AdminCommand adminCommand = (AdminCommand) findMemberCommand;
            member = adminCommand.getMemberResult();
            if (member != null) {
                setMember(member);
                System.out.println("Người đọc tồn tại");
            } else {
                setMemberTextFieldNull();
                System.out.println("Người đọc không tồn tại");
            }
        } else {
            System.out.println("Lỗi truy vấn");
        }
    }

    private void loadBookFindData(String newValue) {
        System.out.println("Load book Find Dtaa");
        bookItem = new BookItem();
        bookItem.setBarcode(Integer.parseInt(newValue));
        System.out.println(bookItem.getBarcode());

        Command findBookCommand = new AdminCommand("find", bookItem);
        commandInvoker.setCommand(findBookCommand);
        if (commandInvoker.executeCommand()) {
            AdminCommand adminCommand = (AdminCommand) findBookCommand;
            bookItem = adminCommand.getBookItemResult();
            if (bookItem != null) {
                setBookItem(bookItem);
                System.out.println("Sách tồn tại");
            } else {
                setBookTextFielNull();
                System.out.println("Sách không tồn tại");
            }
        } else {
            setBookTextFielNull();
            System.out.println("Lỗi truy vấn");
        }
    }

    public void loadStartStatus() {
        setAddMode(false);
        setEditMode(false);
    }

    public void setAddMode(boolean add) {
        this.addMode = add;

        editButton.setVisible(!add);
        addButotn.setVisible(add);

        scanBookButton.setVisible(add);
        scanMemberButton.setVisible(add);

        memberIDText.setEditable(add);
        memberNameText.setEditable(add);
        barCodeText.setEditable(add);

        borowDateText.setEditable(add);
        returnDateText.setEditable(add);
        statusText.setEditable(add);

        scanBookButton.setMouseTransparent(!add);
        scanMemberButton.setMouseTransparent(!add);

        if (add) {
            saveButton.setVisible(!add);
            savePane.setVisible(!add);

            memberIDText.setText(null);
            setMemberTextFieldNull();

            barCodeText.setText(null);
            setBookTextFielNull();
            statusText.setText(null);
            borowDateText.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
            returnDateText.setText(null);
        }

    }

    private void setMemberTextFieldNull() {
        memberNameText.setText(null);
        phoneNumberText.setText(null);
        emailText.setText(null);
        genderText.setText(null);
        totalOFBorrowText.setText(null);
        totalOfLostText.setText(null);
    }

    private void setBookTextFielNull() {
        bookNameText.setText(null);
        categoryText.setText(null);
        authorNameText.setText(null);

    }

    public void setEditMode(boolean edit) {
        editButton.setVisible(!edit);
        saveButton.setVisible(edit);
        deleteButton.setVisible(edit);
        savePane.setVisible(edit);

        memberIDText.setEditable(edit);
        barCodeText.setEditable(edit);

        borowDateText.setEditable(edit);
        returnDateText.setEditable(edit);
        statusText.setEditable(edit);

        scanBookButton.setMouseTransparent(edit);
        scanMemberButton.setMouseTransparent(edit);
    }

    public void setMember(Member member) {
        this.member = member;
        memberIDText.setText(String.valueOf(member.getPerson().getId()));
        memberNameText.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName());
        phoneNumberText.setText(member.getPerson().getPhone());
        emailText.setText(member.getPerson().getEmail());
        genderText.setText(member.getPerson().getGender().toString());
        totalOFBorrowText.setText(String.valueOf(member.getTotalBooksCheckOut()));
        totalOfLostText.setText("chua co");
    }

    private void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
        bookNameText.setText(bookItem.getTitle());
        categoryText.setText(getCategories(bookItem.getCategories()));
        authorNameText.setText(getAuthors(bookItem.getAuthors()));
    }
}
