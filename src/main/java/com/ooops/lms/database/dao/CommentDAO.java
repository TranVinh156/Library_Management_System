package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentDAO implements DatabaseQuery<Comment> {
    private Database database;

    public CommentDAO() {
        database = Database.getInstance();
    }

    // add
    private static final String INSERT_COMMENT = "INSERT INTO Comments (member_ID, ISBN, title, content, rate) VALUES (?, ?, ?, ?, ?)";

    //find
    private static final String FIND_COMMENT = "SELECT * FROM Comments WHERE comment_ID = ?";

    // delete
    private static final String DELETE_COMMENT = "DELETE FROM Comments WHERE comment_ID = ?";

    @Override
    public void add(Comment entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_COMMENT)) {
            preparedStatement.setInt(1, entity.getMemberId());
            preparedStatement.setInt(2, entity.getISBN());
            preparedStatement.setString(3, entity.getTitle());
            preparedStatement.setString(4, entity.getContent());
            preparedStatement.setString(5, String.valueOf(entity.getRate()));

            preparedStatement.executeUpdate();
        }
    }

    // không có sửa comment
    @Override
    public boolean update(Comment entity) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Comment entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_COMMENT)) {
            preparedStatement.setInt(1, entity.getCommentId());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // không cần tìm theo id
    @Override
    public Comment find(Number keywords) throws SQLException {
        return null;
    }

    // chỉ có tìm kiếm theo ISBN và rate
    @Override
    public List<Comment> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        StringBuilder findCommentByCriteria = new StringBuilder("SELECT * FROM Comments WHERE ");

        for (String key : criteria.keySet()) {
            findCommentByCriteria.append(key).append(" = ?").append(" AND ");
        }

        findCommentByCriteria.setLength(findCommentByCriteria.length() - 5);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findCommentByCriteria.toString())) {
            int index = 1;
            for (Object value : criteria.values()) {
                preparedStatement.setInt(index++, Integer.valueOf(value.toString()));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Comment> commentList = new ArrayList<>();
                while (resultSet.next()) {
                    commentList.add(new Comment(resultSet.getInt("comment_ID"), resultSet.getString("title"), resultSet.getString("content"), Integer.valueOf(resultSet.getString("rate")), resultSet.getInt("member_ID"), resultSet.getInt("ISBN")));
                }
                return commentList;
            }
        }
    }

    // Không có tìm tất cả bình luận
    @Override
    public List<Comment> selectAll() throws SQLException {
        return List.of();
    }
}
