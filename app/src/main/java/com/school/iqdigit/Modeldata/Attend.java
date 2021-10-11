package com.school.iqdigit.Modeldata;

public class Attend {
    private String attendence,attendence_date;

    public Attend(String attendence, String attendence_date) {
        this.attendence = attendence;
        this.attendence_date = attendence_date;
    }

    public String getAttendence() {
        return attendence;
    }

    public String getAttendence_date() {
        return attendence_date;
    }
}
