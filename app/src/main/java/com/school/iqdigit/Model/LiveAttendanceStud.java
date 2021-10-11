package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveAttendanceStud {

@SerializedName("class_title")
@Expose
private String classTitle;
@SerializedName("staffname")
@Expose
private String staffname;
@SerializedName("join_time")
@Expose
private String joinTime;

public String getClassTitle() {
return classTitle;
}

public void setClassTitle(String classTitle) {
this.classTitle = classTitle;
}

public String getStaffname() {
return staffname;
}

public void setStaffname(String staffname) {
this.staffname = staffname;
}

public String getJoinTime() {
return joinTime;
}

public void setJoinTime(String joinTime) {
this.joinTime = joinTime;
}

}
