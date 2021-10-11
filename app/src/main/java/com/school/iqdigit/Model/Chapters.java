package com.school.iqdigit.Model;

public class Chapters {
    Integer chapter_id;

    String chapter_name;

    Integer unit_id;

    String unit_name;

    Integer classesid;

    public Chapters(Integer chapter_id, String chapter_name, Integer unit_id, String unit_name, Integer classesid) {
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.unit_id = unit_id;
        this.unit_name = unit_name;
        this.classesid = classesid;
    }

    public Integer getChapter_id() {
        return chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public Integer getClassesid() {
        return classesid;
    }

}
