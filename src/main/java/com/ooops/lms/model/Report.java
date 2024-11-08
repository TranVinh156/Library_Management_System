package com.ooops.lms.model;

import com.ooops.lms.model.enums.ReportStatus;

public class Report {
    int reportID;
    Member member;
    String title;
    String content;
    ReportStatus status;

    public Report() {

    }

    // khi lấy từ csdl
    public Report(int reportID, Member member, String title, String content, ReportStatus status) {
        this.reportID = reportID;
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    // khi tạo report
    public Report(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }
}
