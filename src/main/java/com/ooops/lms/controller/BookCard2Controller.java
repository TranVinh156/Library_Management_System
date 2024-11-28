package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.util.BookManager;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.ooops.lms.controller.BookSuggestionCardController.executor;

public class BookCard2Controller {
    private static final String BOOK_FXML = "/com/ooops/lms/library_management_system/Book-view.fxml";

    private Book book;
    private BookItem bookItem;
    private HistoryController historyController;

    @FXML
    private Label authorNameLabel,bookNameLabel;
    @FXML
    private ImageView bookImage,starImage;
    @FXML
    private VBox vBox;
    @FXML
    Button cancelReservedButton;

    /**
     * thiết lập dữ liệu cho book card
     * @param book book truyền vào card
     */
    public void setData(Book book) {
        this.book =book;
        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        if(book.getRate() == 0) {
            book.setRate(BookManager.getInstance().isContainInAllBooks(book).getRate());
        }
        starImage.setImage(starImage(book.getRate()));

        Task<Image> loadImageTask = BookManager.getInstance().createLoadImageTask(book);

        loadImageTask.setOnSucceeded(event -> bookImage.setImage(loadImageTask.getValue()));

        executor.submit(loadImageTask);
    }

    /**
     * vào sách.
     * @param mouseEvent khi chuột ấn vào
     */
    public void onBookMouseClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(BOOK_FXML);
            fxmlLoader.setLocation(resource);

            VBox newContent = fxmlLoader.load();

            BookController bookController = fxmlLoader.getController();
            if (book != null) {
                bookController.setBook(book);
            } else {
                System.err.println("Book object is null!");
            }

            FXMLLoaderUtil.getInstance().updateContentBox(newContent);

        } catch (IOException e) {
            e.printStackTrace();  // In ra lỗi nếu gặp ngoại lệ
        }
    }

    /**
     * xoá sách đã đặt trước.
     * @param actionEvent khi ấn vào nút
     */
    public void onCancelReservedButtonAction(ActionEvent actionEvent) {
        try {
            historyController.deleteBookReserved(this.bookItem,vBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * nếu là sách đã đặt trước thì hển thị nút x ở trên ảnh.
     * @param historyController controller
     * @param bookItem sách đã đặt trước
     */
    public void setReservedBook(HistoryController historyController,BookItem bookItem) {
        this.historyController = historyController;
        this.bookItem = bookItem;
        cancelReservedButton.setVisible(true);
    }

    /**
     * từ rate sang Image
     * @param numOfStar số rate
     * @return ảnh
     */
    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }
}
