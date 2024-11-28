package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.email.EmailUtil;
import com.ooops.lms.faceid.FaceidLogin;
import com.ooops.lms.faceid.FaceidRecognizer;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class AccountDAO {
    private static Database database;
    private static AccountDAO accountDAO;

    private AccountDAO() {
        database = Database.getInstance();
    }

    /**
     * singleton.
     * @return dao
     */
    public static synchronized AccountDAO getInstance() {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }
        return accountDAO;
    }

    // Lấy thông tin tài khoản admin
    private static final String GET_ACCOUNT_ADMIN =
            "Select * from Admins where username = ? and password = ?";
    private static final String GET_ACCOUNT_ADMIN_BY_ID =
            "Select * from Admins where admin_ID = ?";

    // lấy thông tin tài khoản user
    private static final String GET_ACCOUNT_USER =
            "Select * from Users where username = ? and password = ? and AccountStatus != 'CLOSED'";
    private static final String GET_ACCOUNT_USER_BY_ID =
            "Select * from Users where user_id = ?";

    // Thêm tài khoản user
    private static final String INSERT_USER =
            "Insert into Users (username, password, member_ID) values (?, ?, ?)";

    // Thêm thành viên
    private static final String INSERT_MEMBER =
            "Insert into Members (first_name, last_name, birth_date, gender, email, phone) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    // Lấy thông tin reset password
    private static final String GET_RESET_PASSWORD_USER
            = "Select * from Members join users on users.member_ID = Members.member_ID where email = ?";

    // Lấy thông tin user bằng username
    private static final String GET_USER_BY_USERNAME =
            "Select * from Users where username = ?";

    // Cập nhật tài khoản
    private static final String UPDATE_USER =
            "Update Users join Members on users.member_ID = Members.member_ID set password = ? where email = ?";

    // Cập nhạt otp
    private static final String UPDATE_OTP_AND_EXPIRY =
            "UPDATE Users SET otp = ?, otp_expiry = NOW() + INTERVAL 5 MINUTE WHERE username = ?";

    // Kiểm tra otp
    private static final String VALIDATE_OTP =
            "SELECT * FROM Users join Members on users.member_ID = Members.member_ID WHERE email = ? AND otp = ? AND otp_expiry > NOW()";

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
                return resultSet.getInt("member_id");
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
     * Lấy lại mật khẩu.
     * @param email
     * @return
     * @throws SQLException
     */
    public boolean resetPassword(String email) throws SQLException {
        // Kiểm tra tên dăng nhập và email có trùng khớp
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_RESET_PASSWORD_USER)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { //Nếu trùng khớp
                String OTP = generateOTP();
                String username = resultSet.getString("username");
                saveOTP(username, OTP);
                EmailUtil.sendAsyncEmail(email, "THÔNG BÁO LẤY LẠI MẬT KHẨU", "Mã OTP của bạn là " + OTP);
                return true;
            } else {// Không tồn tại
                throw new SQLException("Account and email do not match");
            }
        }
    }

    /**
     * Lưu otp.
     * @param username tên người dùng
     * @param OTP mã otp
     * @throws SQLException lỗi khi lưu otp
     */
    private void saveOTP(String username, String OTP) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_OTP_AND_EXPIRY)) {
            preparedStatement.setString(1, OTP);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * xác nhận otp.
     * @param email email
     * @param OTP otp
     * @return true nếu đúng và ngược lại
     * @throws SQLException không khớp
     */
    public boolean verifyOTP(String email, String OTP) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(VALIDATE_OTP)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, OTP);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                throw new SQLException("Account and email do not match");
            }
        }
    }

    /**
     * Sinh otp.
     * @return trả về otp
     */
    @NotNull
    private String generateOTP() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }

    /**
     * Đổi mật khẩu
     * @param email email
     * @param oldPassword mật khẩu cũ
     * @param newPassword mật khẩu mới
     * @return true nếu thành công và ngược lại
     * @throws SQLException lỗi
     */
    public boolean changePassword(String email, String oldPassword, String newPassword) throws SQLException {
        try {
            if (validateMemberLogin(email, oldPassword) != 0) {
                try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_USER)) {
                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, email);
                    return preparedStatement.executeUpdate() > 0;
                }
            } else {
                throw new SQLException("Failed to change password");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Đổi mật khẩu
     * @param email email
     * @param newPassword mật khẩu mới
     * @return true nếu thành công và ngược lại
     * @throws SQLException lỗi
     */
    public boolean changePassword(String email, String newPassword) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_USER)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public int userLoginByFaceID() {
        try {
            Integer userID = FaceidLogin.loginFace(FaceidLogin.USER);
            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_ACCOUNT_USER_BY_ID)) {
                preparedStatement.setInt(1, userID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("member_ID");
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int adminLoginByFaceID() {
        try {
            Integer userID = FaceidLogin.loginFace(FaceidLogin.ADMIN);
            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(GET_ACCOUNT_ADMIN_BY_ID)) {
                preparedStatement.setInt(1, userID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("admin_ID");
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
