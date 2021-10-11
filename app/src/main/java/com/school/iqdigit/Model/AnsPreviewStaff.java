package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnsPreviewStaff {

@SerializedName("mcq_id")
@Expose
private Integer mcqId;
@SerializedName("ansstaff")
@Expose
private Integer ansstaff;

public Integer getMcqId() {
return mcqId;
}

public void setMcqId(Integer mcqId) {
this.mcqId = mcqId;
}

public Integer getAnsstaff() {
return ansstaff;
}

public void setAnsstaff(Integer ansstaff) {
this.ansstaff = ansstaff;
}

}