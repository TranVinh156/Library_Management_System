package com.ooops.lms.SuggestionTable;

import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class SuggestionTable {

    @FXML
    private VBox suggestionTable;

    private ScrollPane scrollPane;

    private SuggestionRowClickListener rowClickListener;
    private List<Object> suggestList = new ArrayList<>();
    Map<Integer, Member> uniqueMembersMap = new HashMap<>();
    Map<String, Object> searchCriteria = new HashMap<>();

    public SuggestionTable(ScrollPane scrollPane, VBox suggestionVBox) {
        this.suggestionTable = suggestionVBox;
        this.scrollPane = scrollPane;
    }

    // Thêm setter cho listener
    public void setRowClickListener(SuggestionRowClickListener listener) {
        this.rowClickListener = listener;
    }

    private void loadSuggestionRows() {
        System.out.println("Starting loadMemberSuggestionFindData...");
        //Xóa hết các row cũ trong bảng
        suggestionTable.getChildren().clear();

        //Tạo các row cho mỗi member và đẩy vào bảng
        for (Object o : suggestList) {
            try {
                FXMLLoader loader = new FXMLLoader(SuggestionTable.class.getResource("/com/ooops/lms/library_management_system/AdminSuggestRow.fxml"));
                Node row = loader.load();

                AdminSugesstionRowController rowController = loader.getController();
                rowController.setMainController(this);
                rowController.setSuggestion(o);
                row.setOnMouseClicked(event -> {
                    if (rowClickListener != null) {
                        rowClickListener.onRowClick(o);
                    }
                });
                suggestionTable.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace(); // Bắt các lỗi khác
            }
        }
    }

    public void loadFindData(String typeData, String value) {
        suggestList.clear();
        searchCriteria.clear();
        uniqueMembersMap.clear();
        suggestionTable.getChildren().clear();
        if(value == null || value.isEmpty()) {
            scrollPane.setVisible(false);
            return;
        }
        try {
            switch (typeData) {
                case "memberName":
                    // Tách chuỗi tìm kiếm thành các từ
                    String[] searchTerms = value.toLowerCase().split("\\s+");
                    if(searchTerms.length <= 1) {
                        searchCriteria.put("first_name", searchTerms[0]);
                        for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                            uniqueMembersMap.put(member.getPerson().getId(), member);
                        }
                        searchCriteria.clear();

                        // Tìm theo last_name
                        searchCriteria.put("last_name", searchTerms[0]);
                        for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                            uniqueMembersMap.put(member.getPerson().getId(), member);
                        }
                        searchCriteria.clear();
                    } else {
                            // Tìm theo first_name
                            searchCriteria.put("first_name", searchTerms[0]);
                            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                                uniqueMembersMap.put(member.getPerson().getId(), member);
                            }
                            searchCriteria.clear();

                        String lastName = String.join(" ",
                                Arrays.copyOfRange(searchTerms, 1, searchTerms.length));
                            // Tìm theo last_name
                            searchCriteria.put("last_name",lastName);
                            for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                                uniqueMembersMap.put(member.getPerson().getId(), member);
                            }
                            searchCriteria.clear();
                    }

                    suggestList.addAll(uniqueMembersMap.values());
                    break;
                case "memberID":
                    searchCriteria.put("member_id", value);
                    for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                        uniqueMembersMap.put(member.getPerson().getId(), member);
                    }
                    suggestList.addAll(uniqueMembersMap.values());
                    break;
                case "bookBarCode":
                    searchCriteria.put("book_code", value);
                    break;
                case "bookNameAPI":
                    suggestList.addAll(BookInfoFetcher.searchBooksByKeyword(value));
                    break;
                default:
                    break;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (suggestList.size() > 0) {
            scrollPane.setVisible(true);
            loadSuggestionRows();
        } else {
            scrollPane.setVisible(false);
        }
    }


}
