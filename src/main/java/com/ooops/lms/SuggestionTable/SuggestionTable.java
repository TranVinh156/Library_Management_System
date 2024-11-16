package com.ooops.lms.SuggestionTable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class SuggestionTable {

    @FXML
    private VBox suggestionTable;

    private SuggestionRowClickListener rowClickListener;

    public SuggestionTable(VBox suggestionVBox) {
        this.suggestionTable = suggestionVBox;
    }

    // Thêm setter cho listener
    public void setRowClickListener(SuggestionRowClickListener listener) {
        this.rowClickListener = listener;
    }

    public void loadSuggestionRows(List<Object> suggestList) {
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


}
