package com.ooops.lms.model;

public class Category {
    private int categoryId;
    private String categoryName;

    public Category(int catagoryId, String catagoryName) {
        this.categoryId = catagoryId;
        this.categoryName = catagoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCatagoryName() {
        return categoryName;
    }
}
