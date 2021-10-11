package com.school.iqdigit.Modeldata;

public class Classes {
    private boolean isChecked = false;
    private String class_id, class_name;

    public String getClass_id() {
        return class_id;
    }

    public String getClass_name() {
        return class_name;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public Classes(String class_id, String class_name) {
        this.class_id = class_id;
        this.class_name = class_name;
    }
    public Classes(String class_id, String class_name,boolean isChecked) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.isChecked = isChecked;
    }

}
