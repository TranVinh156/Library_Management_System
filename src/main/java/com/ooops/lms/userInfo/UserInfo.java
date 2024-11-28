package com.ooops.lms.userInfo;

public class UserInfo {
    private String id;
    private String color;
    private boolean isFaceIdSet;

    public UserInfo(String id, String color, boolean isFaceIdSet) {
        this.id = id;
        this.color = color;
        this.isFaceIdSet = isFaceIdSet;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public boolean isFaceIdSet() {
        return isFaceIdSet;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFaceIdSet(boolean faceIdSet) {
        isFaceIdSet = faceIdSet;
    }

    // Phương thức toString để in thông tin của user
    @Override
    public String toString() {
        return "ID: " + id + ", Color: " + color + ", FaceId Set: " + isFaceIdSet;
    }

    // Phương thức để kiểm tra ID người dùng
    public boolean matchesId(String searchId) {
        return this.id.equals(searchId);
    }

    public String toFileString() {
        return id + " " + color + " " + isFaceIdSet;
    }
}
