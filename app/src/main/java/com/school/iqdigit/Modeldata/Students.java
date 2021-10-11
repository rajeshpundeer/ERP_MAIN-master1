package com.school.iqdigit.Modeldata;

public class Students {
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked = false;
    private String stu_id,stu_name;

    public String getStu_id() {
        return stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public Students(String stu_id, String stu_name) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
    }
    public Students(String stu_id, String stu_name,boolean isChecked) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.isChecked = isChecked;
    }
}
