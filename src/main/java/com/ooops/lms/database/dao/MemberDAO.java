package com.ooops.lms.database.dao;

import com.mysql.cj.util.LRUCache;
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

    private static Database database;
    private static LRUCache<Integer, Member> memberCache;

    private MemberDAO() {
        database = Database.getInstance();
        memberCache = new LRUCache<>(100);
    }

    /**
     * singleton.
     * @return dao
     */
    public static synchronized MemberDAO getInstance() {
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
            " where member_ID = ?";

    private static final String UPDATE_ACCOUNT = "Update Users set AccountStatus = ? where user_ID = ?";

    // delete
    private static final String DELETE_MEMBER = "Delete from members where member_ID = ?";
    private static final String DELETE_USER = "Delete from Users where member_ID = ?";

    // find
    private static final String FIND_MEMBER
            = "Select * from members m join users u on m.member_ID = u.member_ID where m.member_ID = ?";

    // select all
    private static final String SELECT_ALL = "Select * from members m join Users u on m.member_ID = u.member_ID";

    /**
     * thêm.
     * @param username tên tài khoản
     * @param password mật khẩu
     * @param memberID id
     * @return true false
     * @throws SQLException lỗi
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

    private String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }

    /**
     * thêm.
     * @param entity thành viên
     * @throws SQLException lỗi
     */
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

    /**
     * thêm tài khoản
     * @param status trạng thái
     * @param memberID id
     * @return true false
     * @throws SQLException lỗi
     */
    private boolean updateAccount(AccountStatus status, int memberID) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_ACCOUNT)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, memberID);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * cập nhật.
     * @param entity thành viên
     * @return true false
     * @throws SQLException lỗi
     */
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
                System.out.println(preparedStatement.toString());

                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_ACCOUNT)) {
                preparedStatement.setString(1, entity.getStatus().name());
                preparedStatement.setInt(2, entity.getAccountId());

                preparedStatement.executeUpdate();
            }

            database.getConnection().commit();
            memberCache.put(entity.getPerson().getId(), entity);
            return true;
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }

    /**
     * xoá.
     * @param entity thành viên
     * @return true false
     * @throws SQLException lỗi
     */
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
            memberCache.remove(entity.getPerson().getId());
            return true;
        } catch (SQLException e) {
            database.getConnection().rollback();
            throw e;
        } finally {
            database.getConnection().setAutoCommit(true);
        }
    }

    /**
     * tìm.
     * @param keywords khoá
     * @return thành viên
     * @throws SQLException lỗi
     */
    @Override
    public Member find(Number keywords) throws SQLException {
        if (memberCache.containsKey(keywords.intValue())) {
            return memberCache.get(keywords.intValue());
        }
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
                            , AccountStatus.valueOf(resultSet.getString("AccountStatus"))
                            , resultSet.getString("added_at_timestamp"), person);
                    memberCache.put(keywords.intValue(), member);
                    return member;
                }
            }
        }

        return null;
    }

    /**
     * tìm.
     * @param criteria tiêu chí
     * @return danh sách thành viên
     * @throws SQLException lỗi
     */
    // search theo id, tên, sđt
    @Override
    public List<Member> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        StringBuilder findMemberByCriteria = new StringBuilder("Select * from members m join users u on m.member_ID = u.member_Id where ");

        for (String key : criteria.keySet()) {
            if (key.equals("member_id")) {
                findMemberByCriteria.append("CAST(m.member_id AS CHAR) like ? OR ");
            } else if (key.equals("fullname")) {
                findMemberByCriteria.append("CONCAT(m.last_name, ' ', m.first_name) LIKE ? OR ");
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
                    members.add(find(resultSet.getInt("member_id")));
                }

                return members;
            }
        }
    }

    /**
     * tất cả.
     * @return tất cả thành viên
     * @throws SQLException lỗi
     */
    @Override
    public List<Member> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Member> members = new ArrayList<>();
                while (resultSet.next()) {
                    members.add(find(resultSet.getInt("member_id")));
                }
                return members;
            }
        }
    }

    /**
     * fetch cache.
     * @param memberID id
     */
    public void fetchCache(int memberID) {
        memberCache.remove(memberID);
    }
}
