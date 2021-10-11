package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqStudAnsWithKey {

@SerializedName("mcq_id")
@Expose
private Integer mcqId;
@SerializedName("ansstud")
@Expose
private Integer ansstud;
@SerializedName("correct_ans")
@Expose
private Integer correctAns;

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

public Integer getCorrectAns() {
return correctAns;
}

public void setCorrectAns(Integer correctAns) {
this.correctAns = correctAns;
}

}