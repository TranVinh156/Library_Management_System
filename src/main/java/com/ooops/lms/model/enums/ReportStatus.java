package com.ooops.lms.model.enums;

public enum ReportStatus {
    PENDING("Chờ xử lý"),
    RESOLVED("Đã xử lý");
    private final String displayName;

    ReportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name(); // Sử dụng tên hiển thị thay vì tên enum
    }
}
