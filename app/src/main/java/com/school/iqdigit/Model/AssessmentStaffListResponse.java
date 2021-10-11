package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssessmentStaffListResponse {
@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("get_assessment_list_root")
@Expose
private List<GetAssessmentListRoot> getAssessmentListRoot = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<GetAssessmentListRoot> getGetAssessmentListRoot() {
return getAssessmentListRoot;
}

public void setGetAssessmentListRoot(List<GetAssessmentListRoot> getAssessmentListRoot) {
this.getAssessmentListRoot = getAssessmentListRoot;
}

}