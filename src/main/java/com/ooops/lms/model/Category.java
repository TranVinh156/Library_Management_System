package com.ooops.lms.model;

public class Category {
    private int categoryId;
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

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
    @Override
    public String toString() {
        return categoryName;
    }
}
