package com.ooops.lms.model.enums;

public enum BookReservationStatus {
    WAITING("Đang chờ"),
    COMPLETED("Đã xong"),
    CANCELED("Đã hủy");

    private final String displayName;

    BookReservationStatus(String displayName) {
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
