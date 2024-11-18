package com.ooops.lms.SuggestionTable;

import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminSugesstionRowController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label textLabel;
    private Object object;
    private SuggestionTable suggestionTable;
    private String text;
    private String ImagePath;

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
        } else if (object instanceof Book) {
            Book book = (Book) object;
            textLabel.setText(book.getTitle() + " - " + book.getISBN());
            imageView.setImage(new Image(book.getImagePath()));
        }

    }
}
