package com.school.iqdigit.Model;

public class Media {
    private int booklet_id;

    private int chapter_id;

    private int subject_id;

    private String book_name;

    private String media_url;

    private String book_desc;

    private String user_type;

    private int user_id;

    private String status;

    public Media(String media_url) {
        this.media_url = media_url;
    }

    public void setBooklet_id(int booklet_id) {
        this.booklet_id = booklet_id;
    }

    public int getBooklet_id() {
        return this.booklet_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getChapter_id() {
        return this.chapter_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSubject_id() {
        return this.subject_id;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_name() {
        return this.book_name;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_url() {
        return this.media_url;
    }

    public void setBook_desc(String book_desc) {
        this.book_desc = book_desc;
    }

    public String getBook_desc() {
        return this.book_desc;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type() {
        return this.user_type;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
