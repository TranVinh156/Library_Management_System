package com.ooops.lms.database.dao;

import com.ooops.lms.database.Database;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportDAO implements DatabaseQuery<Report> {
    private Database database;
    private MemberDAO memberDAO;
    public ReportDAO() {
        database = Database.getInstance();
        memberDAO = new MemberDAO();
    }

    // add
    private static final String ADD_REPORT = "Insert into Reports(member_ID, title, content) values (?, ?, ?)";

    // update
    private static final String UPDATE_REPORT = "Update Reports set title = ?, content = ?, status = ? where report_ID = ?";

    //delete
    private static final String DELETE_REPORT = "Delete from Reports where report_ID = ?";

    //Find Report
    private static final String FIND_REPORT = "Select * from reports where report_id=?";

    // select all
    private static final String SELLECT_ALL = "SELECT * FROM Reports";

    @Override
    public void add(Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(ADD_REPORT)) {
            preparedStatement.setInt(1, entity.getMember().getPerson().getId());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, entity.getContent());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean update(Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(UPDATE_REPORT)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getContent());
            preparedStatement.setString(3, entity.getStatus().name());
            preparedStatement.setInt(4, entity.getReportID());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Report entity) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(DELETE_REPORT)) {
            preparedStatement.setInt(1, entity.getReportID());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public Report find(Number keywords) throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(FIND_REPORT)) {
            preparedStatement.setInt(1, keywords.intValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Report report = new Report(resultSet.getInt("report_ID")
                        , memberDAO.find(resultSet.getInt("member_ID"))
                        , resultSet.getString("title")
                        , resultSet.getString("content")
                        , ReportStatus.valueOf(resultSet.getString("status")));
                return report;
            }
        }
        return null;
    }

    @Override
    public List<Report> searchByCriteria(Map<String, Object> criteria) throws SQLException {
        StringBuilder findReportByCriteria = new StringBuilder("Select * from reports where ");

        for (String key : criteria.keySet()) {
            findReportByCriteria.append(key).append(" like ?").append(" and ");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(findReportByCriteria.toString())) {
            int index = 1;

            for (Object value : criteria.values()) {
                preparedStatement.setString(index++, "%" + value.toString() + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Report> reports = new ArrayList<>();
                while (resultSet.next()) {
                    Report report = new Report(resultSet.getInt("report_ID")
                            , memberDAO.find(resultSet.getInt("member_ID"))
                            , resultSet.getString("title")
                            , resultSet.getString("content")
                            , ReportStatus.valueOf(resultSet.getString("status")));
                    reports.add(report);
                }
                return reports;
            }
        }
    }

    @Override
    public List<Report> selectAll() throws SQLException {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(SELLECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Report> reports = new ArrayList<>();
            while (resultSet.next()) {
                Report report = new Report(resultSet.getInt("report_ID")
                        , memberDAO.find(resultSet.getInt("member_ID"))
                        , resultSet.getString("title")
                        , resultSet.getString("content")
                        , ReportStatus.valueOf(resultSet.getString("status")));
                reports.add(report);
            }
            return reports;
        }
    }
}