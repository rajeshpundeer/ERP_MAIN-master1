package com.school.iqdigit.Modeldata;

public class StudentsPhoto{
    public int stu_id;
    public String stu_name;
    public String pre_image;

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getPre_image() {
        return pre_image;
    }

    public void setPre_image(String pre_image) {
        this.pre_image = pre_image;
    }

    public int getPhoto_editable_stud() {
        return photo_editable_stud;
    }

    public void setPhoto_editable_stud(int photo_editable_stud) {
        this.photo_editable_stud = photo_editable_stud;
    }

    public int getPhoto_editable_staff() {
        return photo_editable_staff;
    }

    public void setPhoto_editable_staff(int photo_editable_staff) {
        this.photo_editable_staff = photo_editable_staff;
    }

    public int photo_editable_stud;
    public int photo_editable_staff;
}