package com.ooops.lms.bookapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import org.json.JSONArray;
import org.json.JSONObject;

public class BookInfoFetcher {
    private static final String API_KEY = "AIzaSyChNsPo2ZSTE80-0Q5mwpLFysHY4zfDqtM";
    private static final String API_URL_KEYWORD = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private static final int MAX_BOOK = 30;

    private static String fetchResponse(URL url) throws Exception {
        HttpURLConnection conn;
        int responseCode;
        int retryCount = 0;
        while (true) {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            responseCode = conn.getResponseCode();

            if (responseCode == 429 && retryCount < 3) {
                System.out.println("Rate limit exceeded. Retrying...");
                Thread.sleep(2000); // Chờ 2 giây trước khi thử lại
                retryCount++;
            } else if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            } else {
                throw new Exception("HTTP error: " + responseCode);
            }
        }
    }


    public static List<Book> searchBooksByKeyword(String title) {
        List<Book> books = new ArrayList<>();
        try {
            // Tạo URL từ tiêu đề
            URL url = new URL(API_URL_KEYWORD + title.replace(" ", "+") + "&key=" + API_KEY);
            String response = fetchResponse(url);

            // Phân tích dữ liệu JSON từ API
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null && items.length() > 0) {
                for (int i = 0; i < items.length(); i++) {
                    if (books.size() >= MAX_BOOK) {
                        break;
                    }

                    JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                    Long isbn = 0L;
                    JSONArray industryIdentifiers = volumeInfo.optJSONArray("industryIdentifiers");
                    if (industryIdentifiers != null) {
                        for (int j = 0; j < industryIdentifiers.length(); j++) {
                            JSONObject identifier = industryIdentifiers.getJSONObject(j);
                            if ("ISBN_13".equals(identifier.optString("type"))) {
                                isbn = identifier.optLong("identifier");
                                break;
                            }
                        }
                    }
                    if (isbn == 0) {
                        continue;
                    }
                    // Lấy thông tin cơ bản
                    String bookTitle = volumeInfo.optString("title", "N/A");
                    String description = volumeInfo.optString("description", "No description available.");
                    String imagePath = volumeInfo.optJSONObject("imageLinks") != null
                            ? volumeInfo.optJSONObject("imageLinks").optString("thumbnail", Book.DEFAULT_IMAGE_PATH)
                            : Book.DEFAULT_IMAGE_PATH;



                    // Lấy danh sách tác giả
                    List<Author> authors = new ArrayList<>();
                    JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                    if (authorsArray != null) {
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authors.add(new Author(authorsArray.getString(j))); // Constructor Author(String)
                        }
                    }

                    // Lấy danh sách thể loại
                    List<Category> categories = new ArrayList<>();
                    JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                    if (categoriesArray != null) {
                        for (int j = 0; j < categoriesArray.length(); j++) {
                            categories.add(new Category(categoriesArray.getString(j))); // Constructor Category(String)
                        }
                    }

                    // Tạo đối tượng Book
                    Book book = new Book(isbn, bookTitle, imagePath, description, "Unknown", authors, categories);
                    books.add(book);
                }
            } else {
                System.out.println("No book information found for title: " + title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public static Book searchBookByISBN(String isbn) {
        try {
            // Tạo URL từ ISBN
            URL url = new URL(API_URL + isbn);
            String response = fetchResponse(url);

            // Phân tích dữ liệu JSON từ API
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                // Lấy thông tin cơ bản
                String title = volumeInfo.optString("title", "N/A");
                String description = volumeInfo.optString("description", "No description available.");
                String imagePath = volumeInfo.optJSONObject("imageLinks") != null
                        ? volumeInfo.optJSONObject("imageLinks").optString("thumbnail", Book.DEFAULT_IMAGE_PATH)
                        : Book.DEFAULT_IMAGE_PATH;

                // Lấy danh sách tác giả
                List<Author> authors = new ArrayList<>();
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                if (authorsArray != null) {
                    for (int i = 0; i < authorsArray.length(); i++) {
                        authors.add(new Author(authorsArray.getString(i))); // Cần định nghĩa constructor Author(String)
                    }
                }

                // Lấy danh sách thể loại
                List<Category> categories = new ArrayList<>();
                JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                if (categoriesArray != null) {
                    for (int i = 0; i < categoriesArray.length(); i++) {
                        categories.add(new Category(categoriesArray.getString(i))); // Cần định nghĩa constructor Category(String)
                    }
                }

                // Tạo và trả về đối tượng Book
                Book book = new Book(Long.parseLong(isbn), title, imagePath, description, "Unknown", authors, categories);
                return book;
            } else {
                System.out.println("No book information found for ISBN: " + isbn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Thay thế ISBN này bằng ISBN bạn muốn tìm kiếm
        String isbn = "9780470046968";
        List<Book> books = searchBooksByKeyword("java");
        for (Book book : books) {
            System.out.println(book.getISBN() + " " + book.getTitle());
        }
    }
}
