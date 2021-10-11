package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqLayout {

@SerializedName("mcq_id")
@Expose
private Integer mcqId;
@SerializedName("mcq_sno")
@Expose
private Integer mcqSno;

public Integer getMcqId() {
return mcqId;
}

public void setMcqId(Integer mcqId) {
this.mcqId = mcqId;
}

public Integer getMcqSno() {
return mcqSno;
}

public void setMcqSno(Integer mcqSno) {
this.mcqSno = mcqSno;
}

}