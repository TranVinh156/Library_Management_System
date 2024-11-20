package com.ooops.lms.SuggestionTable;

import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.enums.BookItemStatus;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuggestionTable {

    @FXML
    private VBox suggestionTable;

    private ScrollPane scrollPane;

    private SuggestionRowClickListener rowClickListener;
    private List<Object> suggestList = new ArrayList<>();
    Map<Integer, Member> uniqueMembersMap = new HashMap<>();
    Map<String, Object> searchCriteria = new HashMap<>();
    private TextField activeTextField;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SuggestionTable(ScrollPane scrollPane, VBox suggestionVBox) {
        this.suggestionTable = suggestionVBox;
        this.scrollPane = scrollPane;
    }

    // Thêm setter cho listener
    public void setRowClickListener(SuggestionRowClickListener listener) {
        this.rowClickListener = listener;
    }

    public void loadSuggestionRowsAsync() {
        Task<List<Node>> loadRowsTask = new Task<>() {
            @Override
            protected List<Node> call() throws Exception {
                List<Node> rows = new ArrayList<>();
                int count = 0;

                for (Object o : suggestList) {
                    if (count == 30) break;

                    try {
                        FXMLLoader loader = new FXMLLoader(SuggestionTable.class.getResource(
                                "/com/ooops/lms/library_management_system/AdminSuggestRow.fxml"));
                        Node row = loader.load();

                        AdminSugesstionRowController rowController = loader.getController();
                        rowController.setMainController(SuggestionTable.this);
                        rowController.setSuggestion(o);

                        final Object suggestion = o;
                        row.setOnMouseClicked(event -> {
                            if (rowClickListener != null) {
                                rowClickListener.onRowClick(suggestion);
                            }
                        });

                        rows.add(row);
                        count++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return rows;
            }
        };

        loadRowsTask.setOnSucceeded(event -> {
            List<Node> rows = loadRowsTask.getValue();
            Platform.runLater(() -> {
                suggestionTable.getChildren().setAll(rows);
            });
        });

        executorService.submit(loadRowsTask);
    }

    private void loadSuggestionRows() {
        System.out.println("Starting loadMemberSuggestionFindData...");
        //Xóa hết các row cũ trong bảng
        suggestionTable.getChildren().clear();
        int count = 0;
        //Tạo các row cho mỗi member và đẩy vào bảng
        for (Object o : suggestList) {
            try {
                if (count == 30) return;
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
        if (value == null || value.isEmpty()) {
            scrollPane.setVisible(false);
            scrollPane.setLayoutX(0);
            scrollPane.setLayoutY(0);
            return;
        }
        try {
            switch (typeData) {
                case "memberName":
                    // Tách chuỗi tìm kiếm thành các từ
                    String[] searchTerms = value.toLowerCase().split("\\s+");
                    if (searchTerms.length <= 1) {
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
                        searchCriteria.put("last_name", lastName);
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
                    searchCriteria.put("title", value);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
                    break;
                case "bookItemName":
                    searchCriteria.put("title", value);
                    searchCriteria.put("status", BookItemStatus.AVAILABLE);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
                    break;
                case "bookAPI":
                    System.out.println("Suggest book API");
                    long startTime = System.currentTimeMillis();

                    suggestList.addAll(BookInfoFetcher.searchBooksByKeyword(value));

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("Thời gian thực thi API: " + duration + " milliseconds");
                    break;
                default:
                    break;
            }
            if (!suggestList.isEmpty()) {
                long startTime = System.currentTimeMillis();
                Platform.runLater(() -> scrollPane.setVisible(true));
                loadSuggestionRowsAsync();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                System.out.println("Thời gian load hàng vào Vbox: " + duration + " milliseconds");
            } else {
                Platform.runLater(() -> {
                    scrollPane.setVisible(false);
                    scrollPane.setLayoutX(0);
                    scrollPane.setLayoutY(0);
                });
            }

        } catch (SQLException e) {
            Platform.runLater(() -> {
                scrollPane.setVisible(false);
                // Xử lý lỗi nếu cần
            });
            throw new RuntimeException(e);
        }

    }

    public void updateSuggestionPaneForActiveField() {
        if (activeTextField != null) {
            updateSuggestionPanePosition(activeTextField);
        }
    }

    public void updateSuggestionPanePosition(TextField textField) {
        activeTextField = textField;
        // Lấy tọa độ của textField trong Scene
        Bounds boundsInScene = textField.localToScene(textField.getBoundsInLocal());

        // Chuyển đổi tọa độ này sang hệ tọa độ của parent chứa suggestionPane
        Bounds boundsInParent = scrollPane.getParent().sceneToLocal(boundsInScene);

        suggestionTable.setPrefWidth(scrollPane.getWidth());
        scrollPane.setMaxWidth(boundsInParent.getWidth());
        scrollPane.setMaxWidth(boundsInParent.getWidth());

        // Cập nhật vị trí của suggestionPane
        scrollPane.setLayoutX(boundsInParent.getMinX());
        scrollPane.setLayoutY(boundsInParent.getMaxY());
    }

}
