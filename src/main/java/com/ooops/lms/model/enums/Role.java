package com.ooops.lms.model.enums;

public enum Role {
    ADMIN("ADMIN"),
    NONE("NONE");
    private final String displayName;

    Role(String displayName) {
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
