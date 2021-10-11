package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckAnsStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("mcq_staff_ans")
@Expose
private List<McqStaffAn> mcqStaffAns = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<McqStaffAn> getMcqStaffAns() {
return mcqStaffAns;
}

public void setMcqStaffAns(List<McqStaffAn> mcqStaffAns) {
this.mcqStaffAns = mcqStaffAns;
}

}