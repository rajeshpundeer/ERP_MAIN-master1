package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqStaffAn {

@SerializedName("mcq_sno")
@Expose
private Integer mcqSno;
@SerializedName("ans")
@Expose
private Integer ans;

public Integer getMcqSno() {
return mcqSno;
}

public void setMcqSno(Integer mcqSno) {
this.mcqSno = mcqSno;
}

public Integer getAns() {
return ans;
}

public void setAns(Integer ans) {
this.ans = ans;
}

}