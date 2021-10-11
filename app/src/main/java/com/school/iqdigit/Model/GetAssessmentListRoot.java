package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAssessmentListRoot {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stud_id")
    @Expose
    private Integer studId;
    @SerializedName("hw_id")
    @Expose
    private Integer hwId;
    @SerializedName("teacher_id")
    @Expose
    private Integer teacherId;
    @SerializedName("assessment_unit")
    @Expose
    private String assessmentUnit;
    @SerializedName("max")
    @Expose
    private String max;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("as_name")
    @Expose
    private String asName;
    @SerializedName("as_description")
    @Expose
    private String asDescription;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("es_subjectname")
    @Expose
    private String esSubjectname;
    @SerializedName("es_detail")
    @Expose
    private String esDetail;
    @SerializedName("es_classname")
    @Expose
    private String esClassname;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("admission_id")
    @Expose
    private String admissionId;

    @SerializedName("mcq_marks")
    @Expose
    private String mcq_marks;

    @SerializedName("mcq_count")
    @Expose
    private String mcq_count;

    @SerializedName("assessment")
    @Expose
    private String assessment;

    public String getMcq_marks() {
        return mcq_marks;
    }

    public void setMcq_marks(String mcq_marks) {
        this.mcq_marks = mcq_marks;
    }

    public String getMcq_count() {
        return mcq_count;
    }

    public void setMcq_count(String mcq_count) {
        this.mcq_count = mcq_count;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudId() {
        return studId;
    }

    public void setStudId(Integer studId) {
        this.studId = studId;
    }

    public Integer getHwId() {
        return hwId;
    }

    public void setHwId(Integer hwId) {
        this.hwId = hwId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getAssessmentUnit() {
        return assessmentUnit;
    }

    public void setAssessmentUnit(String assessmentUnit) {
        this.assessmentUnit = assessmentUnit;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public String getAsDescription() {
        return asDescription;
    }

    public void setAsDescription(String asDescription) {
        this.asDescription = asDescription;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEsSubjectname() {
        return esSubjectname;
    }

    public void setEsSubjectname(String esSubjectname) {
        this.esSubjectname = esSubjectname;
    }

    public String getEsDetail() {
        return esDetail;
    }

    public void setEsDetail(String esDetail) {
        this.esDetail = esDetail;
    }

    public String getEsClassname() {
        return esClassname;
    }

    public void setEsClassname(String esClassname) {
        this.esClassname = esClassname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

}