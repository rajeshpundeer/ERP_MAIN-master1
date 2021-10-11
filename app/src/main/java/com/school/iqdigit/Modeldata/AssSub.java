package com.school.iqdigit.Modeldata;

public class AssSub {
    private String subject_id,subject_name,subject_address,subject_title,subject_description;

    public String getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getSubject_address() {
        return subject_address;
    }

    public String getSubject_title() {
        return subject_title;
    }

    public String getSubject_description() {
        return subject_description;
    }

    public AssSub(String subject_id, String subject_name, String subject_address, String subject_title, String subject_description) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.subject_address = subject_address;
        this.subject_title = subject_title;
        this.subject_description = subject_description;
    }
}
