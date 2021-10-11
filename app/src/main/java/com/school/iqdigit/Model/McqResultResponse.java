package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McqResultResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("mcq_stud_ans_with_key")
@Expose
private List<McqStudAnsWithKey> mcqStudAnsWithKey = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<McqStudAnsWithKey> getMcqStudAnsWithKey() {
return mcqStudAnsWithKey;
}

public void setMcqStudAnsWithKey(List<McqStudAnsWithKey> mcqStudAnsWithKey) {
this.mcqStudAnsWithKey = mcqStudAnsWithKey;
}

}