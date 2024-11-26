package com.ooops.lms.model.enums;

public enum AccountStatus {
    ACTIVE("ACTIVE"),
    BLACKLISTED("BLACKLISTED"),
    CLOSED("CLOSED"),
    NONE("NONE");
    private final String displayName;

    AccountStatus(String displayName) {
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
