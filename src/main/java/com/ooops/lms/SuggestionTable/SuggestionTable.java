package com.ooops.lms.SuggestionTable;

import com.ooops.lms.bookapi.BookInfoFetcher;
import com.ooops.lms.controller.BookSuggestionCardController;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.model.enums.BookReservationStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

    private ListView<HBox> suggestionListView;

    private SuggestionRowClickListener rowClickListener;
    private List<Object> suggestList = new ArrayList<>();
    Map<Integer, Member> uniqueMembersMap = new HashMap<>();
    Map<String, Object> searchCriteria = new HashMap<>();
    private TextField activeTextField;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SuggestionTable(ScrollPane scrollPane, VBox suggestionVBox, ListView<HBox> listView) {
        this.suggestionTable = suggestionVBox;
        this.scrollPane = scrollPane;
        this.suggestionListView = listView;

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
                        if (row instanceof HBox) {
                            HBox cardBox = (HBox) row;
                            cardBox.setMinWidth(200);
                            // cardBox.prefWidthProperty().bind(activeTextField.widthProperty().subtract(16));
                        }
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

    public void loadFindData(String typeData, String value) {
        boolean loaded = false;
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
                    searchCriteria.clear();
                    searchCriteria.put("fullname", value);
                    suggestList.addAll(MemberDAO.getInstance().searchByCriteria(searchCriteria));

                    break;
                case "memberID":
                    searchCriteria.put("member_id", value);
                    for (Member member : MemberDAO.getInstance().searchByCriteria(searchCriteria)) {
                        uniqueMembersMap.put(member.getPerson().getId(), member);
                    }
                    suggestList.addAll(uniqueMembersMap.values());
                    break;
                case "bookBarCode":
                    searchCriteria.put("ISBN", value);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
                    break;
                case "bookItemName":
                    searchCriteria.put("title", value);
                    searchCriteria.put("BookItemStatus", BookItemStatus.AVAILABLE);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
                    break;
                case "bookNameAPI":
                    System.out.println("dhgfd");
                    Task<List<Book>> fetchBooksTask = new Task<>() {
                        @Override
                        protected List<Book> call() throws Exception {
                            try {
                                System.out.println("Starting API call...");
                                List<Book> results = BookInfoFetcher.searchBooksByKeyword(value);
                                System.out.println("API call completed. Found " + (results != null ? results.size() : 0) + " books");
                                return results;
                            } catch (Exception e) {
                                System.out.println("Error in API call: " + e.getMessage());
                                e.printStackTrace();
                                throw e;
                            }
                        }
                    };

                    fetchBooksTask.setOnSucceeded(event -> {
                        try {
                            List<Book> books = fetchBooksTask.getValue();
                            System.out.println("Task succeeded. Adding " + (books != null ? books.size() : 0) + " books to suggestList");
                            if (books != null) {
                                suggestList.addAll(books);
                                Platform.runLater(() -> scrollPane.setVisible(true));
                                loadSuggestionRowsAsync();
                            } else {
                                Platform.runLater(() -> {
                                    scrollPane.setVisible(false);
                                    scrollPane.setLayoutX(0);
                                    scrollPane.setLayoutY(0);
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Error in onSucceeded: " + e.getMessage());

                            e.printStackTrace();
                        }
                    });
                    //List<Book> listbook = BookInfoFetcher.searchBooksByKeyword(value);
                    //suggestList.addAll(listbook);
                    new Thread(fetchBooksTask).start();
                    return;
                case "bookISBNAPI":
                    Book book = BookInfoFetcher.searchBookByISBN(value);
                    if(book!= null) {
                        suggestList.add(book);
                    }
                    break;
                default:
                    break;
            }
            System.out.println(suggestList.size());
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
        loaded = false;

    }

    public void loadFindData(String typeData, String value, String member_ID) {
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
                case "bookBarCode":
                    //Tim dat truoc
                    searchCriteria.put("member_ID", member_ID);
                    searchCriteria.put("barcode", value);
                    searchCriteria.put("BookReservationStatus", BookReservationStatus.WAITING);
                    List<BookReservation> listbook = BookReservationDAO.getInstance().searchByCriteria(searchCriteria);
                    if(listbook.size() > 0) {
                        System.out.println("Co dat truoc ne");
                        for (BookReservation bookReservation : listbook) {
                            suggestList.add(bookReservation.getBookItem());
                        }
                    }
                    searchCriteria.clear();
                    searchCriteria.put("barcode", value);
                    searchCriteria.put("BookItemStatus", BookItemStatus.AVAILABLE);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
                    break;
                case "bookItemName":
                    //Tim dat truoc
                    searchCriteria.put("member_ID", member_ID);
                    searchCriteria.put("title", value);
                    searchCriteria.put("BookReservationStatus", BookReservationStatus.WAITING);
                    List<BookReservation> listbook2 = BookReservationDAO.getInstance().searchByCriteria(searchCriteria);
                    if(listbook2.size() > 0) {
                        System.out.println("Co dat truoc ne");
                        for (BookReservation bookReservation : listbook2) {
                            suggestList.add(bookReservation.getBookItem());
                        }
                    }
                    searchCriteria.clear();
                    searchCriteria.put("title", value);
                    searchCriteria.put("BookItemStatus", BookItemStatus.AVAILABLE);
                    suggestList.addAll(BookItemDAO.getInstance().searchByCriteria(searchCriteria));
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

        scrollPane.setMaxWidth(textField.getWidth());
        scrollPane.setMinWidth(textField.getWidth());

        suggestionTable.setMinWidth(1000);
        suggestionTable.setMaxWidth(1000);

        suggestionListView.setMinWidth(scrollPane.getWidth());
        suggestionListView.setMaxWidth(scrollPane.getWidth());

        scrollPane.setMaxWidth(boundsInParent.getWidth());
        scrollPane.setMaxWidth(boundsInParent.getWidth());

        // Cập nhật vị trí của suggestionPane
        scrollPane.setLayoutX(boundsInParent.getMinX());
        scrollPane.setLayoutY(boundsInParent.getMaxY());
    }

}

