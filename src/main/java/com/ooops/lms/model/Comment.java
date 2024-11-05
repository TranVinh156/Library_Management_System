package com.ooops.lms.model;

public class Comment {
    private int commentId;
    private String title;
    private String content;
    private int rate = 5;
    private int memberId;
    private int ISBN;

    public Comment(int commentId, String title, String content, int rate, int memberId, int ISBN) {
        this.commentId = commentId;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.memberId = memberId;
        this.ISBN = ISBN;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getRate() {
        return rate;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getISBN() {
        return ISBN;
    }
}
