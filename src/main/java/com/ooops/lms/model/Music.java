package com.ooops.lms.model;

public class Music {
    private String musicId;
    private String title;
    private String thumbnail;

    public Music(String musicId, String title, String thumbnail) {
        this.musicId = musicId;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
