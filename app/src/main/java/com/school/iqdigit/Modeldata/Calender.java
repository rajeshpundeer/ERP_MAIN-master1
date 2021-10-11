package com.school.iqdigit.Modeldata;

public class Calender {
    private String title, date, desc, isholiday;

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getIsholiday() {
        return isholiday;
    }

    public Calender(String title, String date, String desc, String isholiday) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.isholiday = isholiday;
    }
}
