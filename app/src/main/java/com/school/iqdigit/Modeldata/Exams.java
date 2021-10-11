package com.school.iqdigit.Modeldata;

public class Exams {
    private String examid , examname;

    public String getExamid() {
        return examid;
    }


    public String getExamname() {
        return examname;
    }

    public Exams(String examid, String examname) {
        this.examid = examid;
        this.examname = examname;
    }
}
