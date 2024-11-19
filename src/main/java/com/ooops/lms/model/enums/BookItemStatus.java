package com.ooops.lms.model.enums;

public enum BookItemStatus {
    AVAILABLE("Có sẵn"),
    RESERVED("Đặt trước"),
    LOANED("Đang mượn"),
    LOST("Làm mất");
    private final String displayName;

    BookItemStatus(String displayName) {
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
