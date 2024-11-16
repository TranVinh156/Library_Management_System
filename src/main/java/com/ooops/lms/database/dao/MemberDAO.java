package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.email.EmailUtil;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;
import com.ooops.lms.model.enums.Gender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MemberDAO implements DatabaseQuery<Member> {
    private static MemberDAO memberDAO;

    private Database database;

    private MemberDAO() {
        database = Database.getInstance();
    }

    public static MemberDAO getInstance() {
        if (memberDAO == null) {
            memberDAO = new MemberDAO();
        }
        return memberDAO;
    }

    // add member
    private static final String INSERT_MEMBER =
            "Insert into Members (first_name, last_name, birth_date, gender, email, phone, image_path) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    // add account
    private static final String INSERT_USER = "Insert into Users (username, password, member_ID) values (?, ?, ?)";

    // update member
    private static final String UPDATE_MEMBER = "Update Members " +
            "set first_name = ?, last_name = ?, birth_date = ?, gender = ?, email = ?, phone = ?, image_path = ?" +
            "where member_ID = ?";

    private static final String UPDATE_ACCOUNT = "Update Users set status = ? where user_ID = ?";

    // delete
    private static final String DELETE_MEMBER = "Delete from members where member_ID = ?";
    private static final String DELETE_USER = "Delete from Users where member_ID = ?";

    // find
    private static final String FIND_MEMBER
            = "Select * from members m join users u on m.member_ID = u.member_ID where m.member_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from members m join Users u on m.member_ID = u.member_ID";

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

    private String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }

    @Override
    public void add(Member entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getPerson().getFirstName());
            preparedStatement.setString(2, entity.getPerson().getLastName());
            preparedStatement.setString(3, entity.getPerson().getDateOfBirth());
            preparedStatement.setString(4, entity.getPerson().getGender().toString());
            preparedStatement.setString(5, entity.getPerson().getEmail());
            preparedStatement.setString(6, entity.getPerson().getPhone());
            preparedStatement.setString(7, entity.getPerson().getImagePath());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int memberId = generatedKeys.getInt(1);
                    String password = generatePassword();
                    if (insertUser(entity.getPerson().getPhone(), password, memberId)) {
                        EmailUtil.sendAsyncEmail(entity.getPerson().getEmail()
                                , "TÀI KHOẢN", "Tài khoản: " + entity.getPerson().getPhone()
                                        + ", Mật khẩu: " + password);
                    } else {
                        throw new SQLException("Failed to insert member");
                    }
                } else {
                    throw new SQLException("Failed to insert member");
                }
            }
        }
    }

    private boolean updateAccount(AccountStatus status, int memberID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_ACCOUNT)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, memberID);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Member entity) throws SQLException {
        try {
            database.getConnection().setAutoCommit(false);

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_MEMBER)) {
                preparedStatement.setString(1, entity.getPerson().getFirstName());
                preparedStatement.setString(2, entity.getPerson().getLastName());
                preparedStatement.setString(3, entity.getPerson().getDateOfBirth());
                preparedStatement.setString(4, entity.getPerson().getGender().toString());
                preparedStatement.setString(5, entity.getPerson().getEmail());
                preparedStatement.setString(6, entity.getPerson().getPhone());
                preparedStatement.setString(7, entity.getPerson().getImagePath());

                preparedStatement.setInt(8, entity.getPerson().getId());
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_ACCOUNT)) {
                preparedStatement.setString(1, entity.getStatus().name());
                preparedStatement.setInt(2, entity.getAccountId());

                preparedStatement.executeUpdate();
            }

            database.getConnection().commit();

            return true;
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }

    // ko dung
    @Override
    public boolean delete(Member entity) throws SQLException {
        try {
            database.getConnection().setAutoCommit(false);

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_USER)) {
                preparedStatement.setInt(1, entity.getPerson().getId());
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_MEMBER)) {
                preparedStatement.setInt(1, entity.getPerson().getId());
                preparedStatement.executeUpdate();
            }

            database.getConnection().commit();
            return true;
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }


    @Override
    public Member find(Number keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_MEMBER)) {
            preparedStatement.setInt(1, keywords.intValue());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Person person = new Person(resultSet.getInt("member_id")
                            , resultSet.getString("first_name")
                            , resultSet.getString("last_name")
                            , resultSet.getString("image_path")
                            , Gender.valueOf(resultSet.getString("gender"))
                            , resultSet.getString("birth_date")
                            , resultSet.getString("email")
                            , resultSet.getString("phone"));
                    Member member = new Member(resultSet.getInt("user_ID")
                            , resultSet.getString("username")
                            , resultSet.getString("password")
                            , AccountStatus.valueOf(resultSet.getString("status"))
                            , resultSet.getString("added_at_timestamp"), person);
                    return member;
                }
            }
        }

        return null;
    }

    // search theo id, tên, sđt
    @Override
    public List<Member> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        StringBuilder findMemberByCriteria = new StringBuilder("Select * from members m join users u on m.member_ID = u.member_Id where ");

        boolean flag = false;
        for (String key : criteria.keySet()) {
            if (key.equals("member_id")) {
                findMemberByCriteria.append("CAST(m.member_id AS CHAR) like ? OR ");
                flag = true;
            } else {
                findMemberByCriteria.append(key).append(" like ?").append(" OR ");
            }
        }

        findMemberByCriteria.setLength(findMemberByCriteria.length() - 4);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findMemberByCriteria.toString())) {
            int index = 1;
            for (Object value : criteria.values()) {
                preparedStatement.setString(index++, "%" + value.toString() + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Member> members = new ArrayList<>();
                while (resultSet.next()) {
                    Person person = new Person(resultSet.getInt("member_id")
                            , resultSet.getString("first_name")
                            , resultSet.getString("last_name")
                            , resultSet.getString("image_path")
                            , Gender.valueOf(resultSet.getString("gender"))
                            , resultSet.getString("birth_date")
                            , resultSet.getString("email")
                            , resultSet.getString("phone"));
                    Member member = new Member(resultSet.getInt("user_ID")
                            , resultSet.getString("username")
                            , resultSet.getString("password")
                            , AccountStatus.valueOf(resultSet.getString("status"))
                            , resultSet.getString("added_at_timestamp"), person);
                    members.add(member);
                }

                return members;
            }
        }
    }

    @Override
    public List<Member> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Member> members = new ArrayList<>();
                while (resultSet.next()) {
                    Person person = new Person(resultSet.getInt("member_id")
                            , resultSet.getString("first_name")
                            , resultSet.getString("last_name")
                            , resultSet.getString("image_path")
                            , Gender.valueOf(resultSet.getString("gender"))
                            , resultSet.getString("birth_date")
                            , resultSet.getString("email")
                            , resultSet.getString("phone"));
                    Member member = new Member(resultSet.getInt("user_ID")
                            , resultSet.getString("username")
                            , resultSet.getString("password")
                            , AccountStatus.valueOf(resultSet.getString("status"))
                            , resultSet.getString("added_at_timestamp"), person);
                    members.add(member);
                }
                return members;
            }
        }
    }
}
