package com.ooops.lms.model.enums;

public enum BookIssueStatus {
    BORROWED ("Đang mượn"),
    RETURNED("Đã trả"),
    LOST ("Làm mất");

    private final String displayName;

    BookIssueStatus(String displayName) {
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
