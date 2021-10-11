package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStudentsbyexamlist {

@SerializedName("stu_id")
@Expose
private Integer stuId;
@SerializedName("stu_name")
@Expose
private String stuName;
@SerializedName("marks")
@Expose
private String marks;
@SerializedName("status")
@Expose
private String status;

public Integer getStuId() {
return stuId;
}

public void setStuId(Integer stuId) {
this.stuId = stuId;
}

public String getStuName() {
return stuName;
}

public void setStuName(String stuName) {
this.stuName = stuName;
}

public String getMarks() {
return marks;
}

public void setMarks(String marks) {
this.marks = marks;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}