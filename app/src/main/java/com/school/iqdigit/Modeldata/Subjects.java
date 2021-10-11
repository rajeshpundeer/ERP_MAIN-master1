package com.school.iqdigit.Modeldata;

public class Subjects {
    private String subject_id, subject_name;

    public String getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public Subjects(String subject_id, String subject_name) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
    }
}
