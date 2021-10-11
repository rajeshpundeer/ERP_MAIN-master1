package com.school.iqdigit.Modeldata;

import java.util.List;

public class StudentDetail {
    private String at_std_class;
    private String attendance_date;
    private String at_std_subject;
    private List<String> at_reg_no = null;
    private List<String> at_attendance = null;
    private List<String> at_stud_name = null;

    public StudentDetail(String at_std_class, String attendance_date, String at_std_subject,
                         List<String> at_reg_no,
                         List<String> at_attendance, List<String> at_stud_name) {
        this.at_std_class = at_std_class;
        this.attendance_date = attendance_date;
        this.at_std_subject = at_std_subject;
        this.at_reg_no = at_reg_no;
        this.at_attendance = at_attendance;
        this.at_stud_name = at_stud_name;
    }

    public String getAt_std_class() {
        return at_std_class;
    }

    public void setAt_std_class(String at_std_class) {
        this.at_std_class = at_std_class;
    }

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getAt_std_subject() {
        return at_std_subject;
    }

    public void setAt_std_subject(String at_std_subject) {
        this.at_std_subject = at_std_subject;
    }

    public List<String> getAt_reg_no() {
        return at_reg_no;
    }

    public void setAt_reg_no(List<String> at_reg_no) {
        this.at_reg_no = at_reg_no;
    }

    public List<String> getAt_attendance() {
        return at_attendance;
    }

    public void setAt_attendance(List<String> at_attendance) {
        this.at_attendance = at_attendance;
    }

    public List<String> getAt_stud_name() {
        return at_stud_name;
    }

    public void setAt_stud_name(List<String> at_stud_name) {
        this.at_stud_name = at_stud_name;
    }


}
