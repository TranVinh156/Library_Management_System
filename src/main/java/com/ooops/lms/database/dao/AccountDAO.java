package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.email.EmailUtil;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AccountDAO {
    private Database database = Database.getInstance();

    private static final String GET_ACCOUNT_ADMIN =
            "Select * from Admins where username = ? and password = ?";

    private static final String GET_ACCOUNT_USER =
            "Select * from Users where username = ? and password = ?";

    private static final String INSERT_USER =
            "Insert into Users (username, password, member_ID) values (?, ?, ?)";

    private static final String INSERT_MEMBER =
            "Insert into Members (first_name, last_name, birth_date, gender, email, phone) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_RESET_PASSWORD_USER
            = "Select * from Members where email = ?";

    private static final String GET_USER_BY_USERNAME =
            "Select * from Users where username = ?";

    private static final String UPDATE_USER =
            "Update Users set password = ? where username = ?";

    private static final String UPDATE_OTP_AND_EXPIRY =
            "UPDATE Users SET otp = ?, otp_expiry = NOW() + INTERVAL 5 MINUTE WHERE username = ?";

    private static final String VALIDATE_OTP =
            "SELECT * FROM Users WHERE username = ? AND otp = ? AND otp_expiry > NOW()";

    /**
     * Kiểm tra thông tin đăng nhập.
     *
     * @param username tên tài khoản
     * @param password mật khẩu
     * @return true nếu trùng khowps và ngược lại
     * @throws SQLException
     */
    public int validateMemberLogin(String username, String password) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_ACCOUNT_USER)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                return 0;
            }
        }
    }

    /**
     * Kiểm tra thông tin đăng nhập.
     *
     * @param username tên tài khoản
     * @param password mật khẩu
     * @return true nếu trùng khớp và ngược lại
     * @throws SQLException
     */
    public int validateAdminLogin(String username, String password) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_ACCOUNT_ADMIN)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("admin_id");
            } else {
                return 0;
            }
        }
    }

    /**
     * Đăng kí tài khoản member.
     * @param person thông tin người dùng
     * @param username tên tài khoản
     * @param password mật khẩu
     * @return true nếu thành công và ngược lại
     * @throws SQLException
     */
    public boolean registerMember(@NotNull Person person, String username, String password) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_USER_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                throw new SQLException("User already exists");
            } else {
                try (PreparedStatement updateStatement
                             = database.getConnection().prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)) {
                    updateStatement.setString(1, person.getFirstName());
                    updateStatement.setString(2, person.getLastName());
                    updateStatement.setString(3, person.getDateOfBirth());
                    updateStatement.setString(4, person.getGender().toString());
                    updateStatement.setString(5, person.getEmail());
                    updateStatement.setString(6, person.getPhone());

                    updateStatement.executeUpdate();
                    try (ResultSet generatedKeys = updateStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int memberId = generatedKeys.getInt(1);
                            return insertUser(username, password, memberId);
                        } else {
                            throw new SQLException("Failed to insert member");
                        }
                    }
                }
            }
        }
    }

    /**
     * Thêm tài khoản.
     * @param username tên tài khoản
     * @param password mật khẩu
     * @param memberID member id
     * @return true nếu thành công
     * @throws SQLException
     */
    private boolean insertUser(String username, String password, int memberID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, memberID);

            if (preparedStatement.executeUpdate() == 1) {
                return true;
            } else {
                throw new SQLException("Failed to insert member");
            }
        }
    }

    /**
     *
     * @param username
     * @param email
     * @return
     * @throws SQLException
     */
    public boolean resetPassword(String username, String email) throws SQLException {
        // Kiểm tra tên dăng nhập và email có trùng khớp
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_RESET_PASSWORD_USER)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { //Nếu trùng khớp
                String OTP = generateOTP();
                saveOTP(username, OTP);
                EmailUtil.sendAsyncEmail(email, "THÔNG BÁO LẤY LẠI MẬT KHẨU", "Mã OTP của bạn là " + OTP);
                return true;
            } else {// Không tồn tại
                throw new SQLException("Account and email do not match");
            }
        }
    }

    private void saveOTP(String username, String OTP) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_OTP_AND_EXPIRY)) {
            preparedStatement.setString(1, OTP);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    private boolean verifyOTP(String username, String OTP) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(VALIDATE_OTP)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, OTP);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                throw new SQLException("Account and email do not match");
            }
        }
    }

    @NotNull
    private String generateOTP() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        try {
            if (validateMemberLogin(username, oldPassword)) {
                try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_USER)) {
                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, username);
                    return preparedStatement.executeUpdate() > 0;
                }
            } else {
                throw new SQLException("Failed to change password");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_USER)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            return preparedStatement.executeUpdate() > 0;
        }
    }

}
