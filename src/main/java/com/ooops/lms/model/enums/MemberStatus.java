package com.ooops.lms.model.enums;

public enum MemberStatus {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    NONE("NONE");
    private final String displayName;

    MemberStatus(String displayName) {
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
