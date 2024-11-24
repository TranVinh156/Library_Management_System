package com.ooops.lms.model;

public class Comment {
    private int commentId;
    private String title;
    private String content;
    private int rate = 5;
    private Member member;
    private long ISBN;

    public Comment(String title, String content, int rate, Member member, long ISBN) {
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.member = member;
        this.ISBN = ISBN;
    }

    public Comment(int commentId, String title, String content, int rate, Member member, long ISBN) {
        this.commentId = commentId;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }
}
