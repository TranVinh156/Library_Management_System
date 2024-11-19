package com.ooops.lms.database;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class Database {
    private static Database database;
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static final String URL = "jdbc:mysql://localhost:3306/lms_database";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Database() {

    }

    /**
     * Đảm bảo chỉ có một kết nối tới cơ sở dữ liệu.
     * @return Trả ề database duy nhất
     */
    public static synchronized Database getInstance() {
        if (database == null) {
            database = new Database();
            database.createConnection();
        }
        return database;
    }

    /**
     * Tạo kết nối với database.
     */
    public void createConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            System.exit(1);
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public boolean execute(String query) {
        try {
            statement = connection.createStatement();
            statement.execute(query);
            System.out.println("Query executed");
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception in Query: " + query + " #####");
            System.out.println("Exception : " + ex.getMessage());
            return false;
        }
    }

    /**
     * Thực hiện truy vấn không tham số.
     * @param query Câu truy vấn
     * @return ResultSet tương ứng
     */
    public ResultSet executeQuery(String query) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            System.out.println("Query executed");
            return resultSet;
        } catch (SQLException ex) {
            System.out.println("Exception in Query: " + query + " #####");
            System.out.println("Exception : " + ex.getMessage());
            return null;
        }
    }

    /**
     * Tạo câu truy vấn có tham số.
     * @param query câu truy vấn
     * @return Câu truy vấn
     */
    public PreparedStatement getPreparedStatement(String query) {
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement;
        } catch (SQLException ex) {
            System.out.println("Exception in getPrepareStatement: " + query + " #####");
            System.out.println("Exception : " + ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param query câu truy vấn
     * @return
     */
    public ResultSet getScrollableResultSet(String query) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException ex) {
            System.out.println("Exception in getScrollableResultSet: " + query + " #####");
            System.out.println("Exception : " + ex.getMessage());
            return null;
        }
    }

    /**
     * Lấy connection.
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Đóng kết nối.
     */
    public void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
