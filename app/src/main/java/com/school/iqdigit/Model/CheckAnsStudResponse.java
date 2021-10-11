package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckAnsStudResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("mcq_stud_ans")
@Expose
private List<McqStudAn> mcqStudAns = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<McqStudAn> getMcqStudAns() {
return mcqStudAns;
}

public void setMcqStudAns(List<McqStudAn> mcqStudAns) {
this.mcqStudAns = mcqStudAns;
}

}