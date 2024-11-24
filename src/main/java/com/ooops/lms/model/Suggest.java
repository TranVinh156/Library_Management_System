package com.ooops.lms.model;

public class Suggest {
    private String imagePath;
    private String text;
    private String id;

    public Suggest(String imagePath, String text, String id) {
        this.imagePath = imagePath;
        this.text = text;
        this.id = id;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
