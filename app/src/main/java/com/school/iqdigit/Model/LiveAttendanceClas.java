package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveAttendanceClas {

@SerializedName("studname")
@Expose
private String studname;
@SerializedName("classes")
@Expose
private Integer classes;

public String getStudname() {
return studname;
}

public void setStudname(String studname) {
this.studname = studname;
}

public Integer getClasses() {
return classes;
}

public void setClasses(Integer classes) {
this.classes = classes;
}

}