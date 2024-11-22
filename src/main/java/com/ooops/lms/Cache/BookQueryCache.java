package com.ooops.lms.Cache;

import com.google.common.cache.Cache;
import com.ooops.lms.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookQueryCache extends QueryCache<String, List<Book>> {

    public BookQueryCache(int capacity) {
        super(capacity);
    }

    public List<Book> findBookInCache(String query, String key) {
        List<Book> books = this.getQueryResult(query);
        List<Book> booksResult = new ArrayList<Book>();
        if (books == null) {
            return null;
        }

        key = key.toLowerCase();

        // Lọc sách theo tiêu chí
        for (Book book : books) {
            if ("title".equalsIgnoreCase(query) && book.getTitle().toLowerCase().contains(key)) {
                booksResult.add(book);
            } else if ("ISBN".equalsIgnoreCase(query) && String.valueOf(book.getISBN()).toLowerCase().contains(key)) {
                booksResult.add(book);
            }
        }

        return booksResult;
    }

}
