package com.school.iqdigit.Model;

public class Assessment_list_staff {
    private String pending;

    private String submitted;

    private String checked;

    private int hw_id;

    private int teacher_id;

    private String assessment_unit;

    private String max;

    private String created_at;

    private String as_name;

    private String as_description;

    private String teacher_name;

    private String es_subjectname;

    private String es_detail;

    private String es_classname;

    private String start_datetime;

    private String end_datetime;

    private String mcq_marks;

    private String mcq_count;

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

    private String assessment;

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }

    public int getTime_bound() {
        return time_bound;
    }

    public void setTime_bound(int time_bound) {
        this.time_bound = time_bound;
    }

    private int time_bound;

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getPending() {
        return this.pending;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getSubmitted() {
        return this.submitted;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getChecked() {
        return this.checked;
    }

    public void setHw_id(int hw_id) {
        this.hw_id = hw_id;
    }

    public int getHw_id() {
        return this.hw_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getTeacher_id() {
        return this.teacher_id;
    }

    public void setAssessment_unit(String assessment_unit) {
        this.assessment_unit = assessment_unit;
    }

    public String getAssessment_unit() {
        return this.assessment_unit;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMax() {
        return this.max;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setAs_name(String as_name) {
        this.as_name = as_name;
    }

    public String getAs_name() {
        return this.as_name;
    }

    public void setAs_description(String as_description) {
        this.as_description = as_description;
    }

    public String getAs_description() {
        return this.as_description;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_name() {
        return this.teacher_name;
    }

    public void setEs_subjectname(String es_subjectname) {
        this.es_subjectname = es_subjectname;
    }

    public String getEs_subjectname() {
        return this.es_subjectname;
    }

    public void setEs_detail(String es_detail) {
        this.es_detail = es_detail;
    }

    public String getEs_detail() {
        return this.es_detail;
    }

    public void setEs_classname(String es_classname) {
        this.es_classname = es_classname;
    }

    public String getEs_classname() {
        return this.es_classname;
    }
}
