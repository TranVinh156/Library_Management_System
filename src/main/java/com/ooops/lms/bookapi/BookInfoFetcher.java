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
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public static Book searchBookByISBN(String isbn) {
        try {
            // Tạo URL từ ISBN
            URL url = new URL(API_URL + isbn);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Đọc dữ liệu từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

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
    }
}
