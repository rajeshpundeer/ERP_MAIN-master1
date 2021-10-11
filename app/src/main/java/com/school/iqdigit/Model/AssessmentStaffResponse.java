package com.school.iqdigit.Model;

import java.util.List;

public class AssessmentStaffResponse
{
    private boolean error;

    private List<Assessment_list_staff> assessment_list_staff;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setAssessment_list_staff(List<Assessment_list_staff> assessment_list_staff){
        this.assessment_list_staff = assessment_list_staff;
    }
    public List<Assessment_list_staff> getAssessment_list_staff(){
        return this.assessment_list_staff;
    }
}
