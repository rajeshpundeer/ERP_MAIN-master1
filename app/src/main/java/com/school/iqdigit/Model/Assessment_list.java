package com.school.iqdigit.Model;

public class Assessment_list {
    private int id;

    private int hw_id;

    private int teacher_id;

    private int stud_id;

    private String stage;

    private String assessment_unit;

    private String max;

    private String score;

    private String details;

    private String path;

    private String created_at;

    private String submitted_at;

    private String checked_at;

    private String as_name;

    private String as_description;

    private String teacher_name;

    private String es_subjectname;

    private String es_detail;

    private String check_remarks;

    private String start_datetime;

    private String end_datetime;

    private int time_bound;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
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

    public void setStud_id(int stud_id) {
        this.stud_id = stud_id;
    }

    public int getStud_id() {
        return this.stud_id;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return this.stage;
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

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return this.score;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setSubmitted_at(String submitted_at) {
        this.submitted_at = submitted_at;
    }

    public String getSubmitted_at() {
        return this.submitted_at;
    }

    public void setChecked_at(String checked_at) {
        this.checked_at = checked_at;
    }

    public String getChecked_at() {
        return this.checked_at;
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

    public void setCheck_remarks(String check_remarks) {
        this.check_remarks = check_remarks;
    }

    public String getCheck_remarks() {
        return this.check_remarks;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getStart_datetime() {
        return this.start_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }

    public String getEnd_datetime() {
        return this.end_datetime;
    }

    public void setTime_bound(int time_bound) {
        this.time_bound = time_bound;
    }

    public int getTime_bound() {
        return this.time_bound;
    }
}
