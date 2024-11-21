package com.ooops.lms.SuggestionTable;

import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Member;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.checkerframework.checker.units.qual.K;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminSugesstionRowController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label textLabel;
    private Object object;
    private SuggestionTable suggestionTable;
    private String text;
    private String ImagePath;
    protected static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void setMainController(SuggestionTable suggest) {
        suggestionTable = suggest;
    }

    public void setSuggestion(Object o) {
        this.object = o;
        if (object instanceof Member) {
            textLabel.setText(((Member) object).getPerson().getFirstName() + " " + ((Member) object).getPerson().getLastName()
                    + " - " + ((Member) object).getPerson().getId());
            imageView.setImage(new Image(((Member) object).getPerson().getImagePath()));
        } else if (object instanceof BookItem) {
            BookItem bookItem = (BookItem) object;
            textLabel.setText(bookItem.getTitle() + " - " + bookItem.getBarcode());
            imageView.setImage(new Image(bookItem.getImagePath()));
        } else if(object instanceof Map<?,?>) {
            // Safely cast the object to a Map
            Map<?, ?> map = (Map<?, ?>) object;

            // Assuming 'first' is a key of type Book
            Book book = (Book) map.get(1);
            Image image = (Image) map.get(2);

            if (book != null) {
                textLabel.setText(book.getTitle() + " - " + book.getISBN());
                imageView.setImage(image);
            }
        }
        else if (object instanceof Book) {
            Book book = (Book) object;
            textLabel.setText(book.getTitle() + " - " + book.getISBN());

            // Tải ảnh bất đồng bộ
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    try {
                        return new Image(book.getImagePath(), true);
                    } catch (Exception e) {
                        System.out.println("Length: " + book.getImagePath().length());

                        File file = new File("bookImage/default.png");
                        return new Image(file.toURI().toString());
                    }
                }
            };

            loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));

            executor.submit(loadImageTask);
        }

    }
}
