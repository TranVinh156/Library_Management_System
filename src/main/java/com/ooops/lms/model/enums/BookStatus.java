package com.ooops.lms.model.enums;

public enum BookStatus {
    AVAILABLE("Có sẵn"),
    UNAVAILAVBLE("Hết hàng");

    private final String displayName;
    BookStatus(String displayName) {
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
