package com.school.iqdigit.Modeldata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqStudAns {

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

public McqStudAns(Integer mcqId,Integer ansstud)
{
    mcqId = this.mcqId;
    ansstud = this.ansstud;
}

}