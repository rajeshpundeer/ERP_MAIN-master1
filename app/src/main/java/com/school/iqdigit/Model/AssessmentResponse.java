package com.school.iqdigit.Model;

import java.util.List;

public class AssessmentResponse {
    private boolean error;

    private List<Assessment_list> assessment_list;

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return this.error;
    }

    public void setAssessment_list(List<Assessment_list> assessment_list) {
        this.assessment_list = assessment_list;
    }

    public List<Assessment_list> getAssessment_list() {
        return this.assessment_list;
    }
}