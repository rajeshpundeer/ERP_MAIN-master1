package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetexamlistMarksentry {

@SerializedName("exam_detailsid")
@Expose
private String examDetailsid;
@SerializedName("exam_name_date")
@Expose
private String examNameDate;

public String getExamDetailsid() {
return examDetailsid;
}

public void setExamDetailsid(String examDetailsid) {
this.examDetailsid = examDetailsid;
}

public String getExamNameDate() {
return examNameDate;
}

public void setExamNameDate(String examNameDate) {
this.examNameDate = examNameDate;
}

}