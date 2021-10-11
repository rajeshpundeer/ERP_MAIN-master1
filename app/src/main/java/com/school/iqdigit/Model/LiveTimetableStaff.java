package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveTimetableStaff {

    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("es_classname")
    @Expose
    private String esClassname;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public LiveTimetableStaff(String subjectName, String startTime, String endTime, String remarks, String esClassname) {
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remarks = remarks;
        this.esClassname = esClassname;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEsClassname() {
        return esClassname;
    }

    public void setEsClassname(String esClassname) {
        this.esClassname = esClassname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}