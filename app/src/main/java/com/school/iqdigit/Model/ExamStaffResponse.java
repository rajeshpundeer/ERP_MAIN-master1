package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("getexamlist_marksentry")
@Expose
private List<GetexamlistMarksentry> getexamlistMarksentry = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<GetexamlistMarksentry> getGetexamlistMarksentry() {
return getexamlistMarksentry;
}

public void setGetexamlistMarksentry(List<GetexamlistMarksentry> getexamlistMarksentry) {
this.getexamlistMarksentry = getexamlistMarksentry;
}

}