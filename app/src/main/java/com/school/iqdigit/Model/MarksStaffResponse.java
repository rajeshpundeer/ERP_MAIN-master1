package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarksStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("getStudentsbyexamlist")
@Expose
private List<GetStudentsbyexamlist> getStudentsbyexamlist = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<GetStudentsbyexamlist> getGetStudentsbyexamlist() {
return getStudentsbyexamlist;
}

public void setGetStudentsbyexamlist(List<GetStudentsbyexamlist> getStudentsbyexamlist) {
this.getStudentsbyexamlist = getStudentsbyexamlist;
}

}