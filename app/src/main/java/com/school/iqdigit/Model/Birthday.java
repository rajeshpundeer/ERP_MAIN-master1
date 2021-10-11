package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Birthday {

@SerializedName("stu_id")
@Expose
private Integer stuId;
@SerializedName("adm_id")
@Expose
private String admId;
@SerializedName("stud_name")
@Expose
private String studName;
@SerializedName("dob")
@Expose
private String dob;
@SerializedName("fname")
@Expose
private String fname;
@SerializedName("mname")
@Expose
private String mname;
@SerializedName("roll")
@Expose
private String roll;
@SerializedName("photo")
@Expose
private String photo;
@SerializedName("class")
@Expose
private String _class;

public Integer getStuId() {
return stuId;
}

public void setStuId(Integer stuId) {
this.stuId = stuId;
}

public String getAdmId() {
return admId;
}

public void setAdmId(String admId) {
this.admId = admId;
}

public String getStudName() {
return studName;
}

public void setStudName(String studName) {
this.studName = studName;
}

public String getDob() {
return dob;
}

public void setDob(String dob) {
this.dob = dob;
}

public String getFname() {
return fname;
}

public void setFname(String fname) {
this.fname = fname;
}

public String getMname() {
return mname;
}

public void setMname(String mname) {
this.mname = mname;
}

public String getRoll() {
return roll;
}

public void setRoll(String roll) {
this.roll = roll;
}

public String getPhoto() {
return photo;
}

public void setPhoto(String photo) {
this.photo = photo;
}

public String getClass_() {
return _class;
}

public void setClass_(String _class) {
this._class = _class;
}

}