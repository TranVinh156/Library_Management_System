package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportDAO implements DatabaseQuery<Report> {
    private static ReportDAO reportDAO;

    private static Database database;
    private static MemberDAO memberDAO;

    private ReportDAO() {
        database = Database.getInstance();
        memberDAO = MemberDAO.getInstance();
    }

    /**
     * singleton.
     * @return dao
     */
    public static synchronized ReportDAO getInstance() {
        if (reportDAO == null) {
            reportDAO = new ReportDAO();
        }
        return reportDAO;
    }

    // add
    private static final String ADD_REPORT = "Insert into Reports(member_ID, title, content) values (?, ?, ?)";

    // update
    private static final String UPDATE_REPORT = "Update Reports set title = ?, content = ?, ReportStatus = ? where report_ID = ?";

    //delete
    private static final String DELETE_REPORT = "Delete from Reports where report_ID = ?";

    //Find Report
    private static final String FIND_REPORT = "Select * from reports where report_id=?";

    // select all
    private static final String SELLECT_ALL = "SELECT * FROM Reports";

    /**
     * thêm.
     * @param entity báo cáo
     * @throws SQLException lỗi
     */
    @Override
    public void add(@NotNull Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_REPORT)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, entity.getContent());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * cập nhật.
     * @param entity báo cáo
     * @return true false
     * @throws SQLException lỗi
     */
    @Override
    public boolean update(@NotNull Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_REPORT)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getContent());
            preparedStatement.setString(3, entity.getStatus().name());
            preparedStatement.setInt(4, entity.getReportID());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * xoá.
     * @param entity báo cáo
     * @return true false
     * @throws SQLException lỗi
     */
    @Override
    public boolean delete(@NotNull Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_REPORT)) {
            preparedStatement.setInt(1, entity.getReportID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * tìm.
     * @param keywords khoá
     * @return báo cáo
     * @throws SQLException lỗi
     */
    @Override
    public Report find(@NotNull Number keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_REPORT)) {
            preparedStatement.setInt(1, keywords.intValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Report(resultSet.getInt("report_ID")
                        , memberDAO.find(resultSet.getInt("member_ID"))
                        , resultSet.getString("title")
                        , resultSet.getString("content")
                        , ReportStatus.valueOf(resultSet.getString("ReportStatus")));
            }
        }
        return null;
    }

    /**
     * tìm.
     * @param criteria tiêu chí
     * @return danh sách báo cáo
     * @throws SQLException lỗi
     */
    @Override
    public List<Report> searchByCriteria(@NotNull Map<String, Object> criteria) throws SQLException {
        StringBuilder findReportByCriteria = new StringBuilder("SELECT * FROM reports JOIN Members on reports.member_ID = Members.member_ID WHERE ");
        Iterator<String> iterator = criteria.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals("fullname")) {
                findReportByCriteria.append("CONCAT(Members.last_name, ' ', Members.first_name) LIKE ?");
            } else {
                findReportByCriteria.append(key).append(" LIKE ?");
            }
            if (iterator.hasNext()) {
                findReportByCriteria.append(" AND ");
            }
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findReportByCriteria.toString())) {
            int index = 1;

            for (Object value : criteria.values()) {
                if (value != null) {
                    preparedStatement.setString(index++, "%" + value.toString() + "%");
                } else {
                    preparedStatement.setString(index++, "%"); // Hoặc giá trị mặc định
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Report> reports = new ArrayList<>();
                while (resultSet.next()) {
                    reports.add(find(resultSet.getInt("report_ID")));
                }
                return reports;
            }
        }
    }

    /**
     * tất cả.
     * @return tất cả báo cáo
     * @throws SQLException lỗi
     */
    @Override
    public List<Report> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELLECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Report> reports = new ArrayList<>();
            while (resultSet.next()) {
                reports.add(find(resultSet.getInt("report_ID")));
            }
            return reports;
        }
    }
}
