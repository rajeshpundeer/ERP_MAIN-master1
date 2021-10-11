package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnsPreviewStud {

@SerializedName("mcq_id")
@Expose
private Integer mcqId;
@SerializedName("ansstud")
@Expose
private Integer ansstud;

public Integer getMcqId() {
return mcqId;
}

public void setMcqId(Integer mcqId) {
this.mcqId = mcqId;
}

public Integer getAnsstud() {
return ansstud;
}

public void setAnsstud(Integer ansstud) {
this.ansstud = ansstud;
}

}