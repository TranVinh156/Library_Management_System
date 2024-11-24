package com.ooops.lms.SuggestionTable;

import com.ooops.lms.Cache.ImageCache;
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
            Member member = (Member) object;
            textLabel.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName() + " - " + member.getPerson().getId());
            // Tải ảnh bất đồng bộ
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    // Nếu như ảnh của member mà không có hoặc đường dẫn ảnh lỗi thì set mặc định
                    try {
                        File file = new File(member.getPerson().getImagePath());
                        return new Image(file.toURI().toString());
                    } catch (Exception e) {
                        return new Image(getClass().getResourceAsStream("/image/avatar/default.png"));
                    }
                }
            };

            loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));

            executor.submit(loadImageTask);


        } else if (object instanceof BookItem) {
            BookItem bookItem = (BookItem) object;
            textLabel.setText(bookItem.getTitle() + " - " + bookItem.getBarcode());
            // Tải ảnh bất đồng bộ
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    try {
                        Image image = ImageCache.getImageLRUCache().get(bookItem.getImagePath());
                        if(image != null) {
                            System.out.println("tai anh trong cache");
                            return image;
                        } else {
                            System.out.println("Khong co anh trong cache");
                            Image image1 = new Image(bookItem.getImagePath(), true);
                            ImageCache.getImageLRUCache().put(bookItem.getImagePath(), image1);
                            return new Image(image1.getUrl());
                        }
                    } catch (Exception e) {
                        System.out.println("Length: " + bookItem.getImagePath().length());

                        File file = new File("bookImage/default.png");
                        return new Image(file.toURI().toString());
                    }
                }
            };

            loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));

            executor.submit(loadImageTask);
        } else if (object instanceof Book) {
            Book book = (Book) object;
            textLabel.setText(book.getTitle() + " - " + book.getISBN());

            // Tải ảnh bất đồng bộ
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    try {
                        Image image = ImageCache.getImageLRUCache().get(book.getImagePath());
                        if(image != null) {
                            System.out.println("tai anh trong cache");
                            return image;
                        } else {
                            System.out.println("Khong co anh trong cache");
                            Image image1 = new Image(book.getImagePath(), true);
                            ImageCache.getImageLRUCache().put(book.getImagePath(), image1);
                            return new Image(image1.getUrl());
                        }
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
