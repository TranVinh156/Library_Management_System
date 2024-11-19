package com.ooops.lms.controller;

import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.print.DocFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class BookSuggestionCardController {
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String BOOK_FXML = "/com/ooops/lms/library_management_system/Book-view.fxml";

    private Book book;
    @FXML
    private Label authorNameLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private HBox hBox;

    @FXML
    private ImageView starImage;

    private String imagePath="";

    public void setData(Book book) {
        this.book = book;
        saveAPIBookImageToLocal(book.getImagePath());
        book.setImagePath(imagePath);
        book.setRate(5);
        File file = new File(imagePath);
        bookImage.setImage(new Image(file.toURI().toString()));

        bookNameLabel.setText(book.getTitle());
        String author = "";
        List<Author> authorList = book.getAuthors();
        for(int i = 0;i<authorList.size();i++) {
            author += authorList.get(i).getAuthorName() + ",";
        }
        authorNameLabel.setText(author);
        starImage.setImage(starImage(book.getRate()));
        book.setQuantity(100);
        addBookToDatabase(book);
    }

    public void onBookMouseClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(BOOK_FXML);
            fxmlLoader.setLocation(resource);

            VBox newContent = fxmlLoader.load();

            BookController bookController = fxmlLoader.getController();
            if (book != null) {
                bookController.setBookByAPIData(book);
            } else {
                System.err.println("Book object is null!");
            }

            fxmlLoaderUtil.updateContentBox(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBookToDatabase(Book book) {
        try {
            BookDAO.getInstance().add(book);
        } catch (SQLException e) {
            System.out.println("book exist");
        }
    }

    private void saveAPIBookImageToLocal(String apiPath) {
        try {
            URL imageUrl = new URL(apiPath);

            InputStream inputStream = imageUrl.openStream();

            BufferedImage image = ImageIO.read(inputStream);

            if (image == null) {
                System.err.println("Failed to load image from URL: " + apiPath);
                return;
            }

            String fileName = book.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";

            Path folderPath = Paths.get("Library_Management_System", "bookImage");
            if (Files.notExists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            Path destinationPath = folderPath.resolve(fileName);
            imagePath = destinationPath.toString();

            ImageIO.write(image, "jpg", destinationPath.toFile());

            System.out.println("Image saved as: " + destinationPath.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image starImage(int numOfStar) {
        String imagePath = "/image/book/" + numOfStar + "Star.png";
        if (getClass().getResourceAsStream(imagePath) == null) {
            System.out.println("Image not found: " + imagePath);
            return new Image(getClass().getResourceAsStream("/image/book/1Star.png"));
        }
        return new Image(getClass().getResourceAsStream(imagePath));
    }
}
