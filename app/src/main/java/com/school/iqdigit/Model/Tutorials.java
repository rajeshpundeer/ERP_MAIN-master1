package com.school.iqdigit.Model;

public class Tutorials {
    private int tut_id;

    private int chapter_id;

    private String title;

    private String file_path;

    private String tut_desc;

    private String lesson;

    private String summary;

    private String user_type;

    private String status;

    public Tutorials(int tut_id,int chapter_id,String title,String file_path,String tut_desc,String lesson,String summary,String user_type,String status){
        this.tut_id = tut_id;
        this.chapter_id = chapter_id;
        this.title = title;
        this.file_path = file_path;
        this.tut_desc = tut_desc;
        this.lesson = lesson;
        this.summary = summary;
        this.user_type = user_type;
        this.status = status;
    }

    public void setTut_id(int tut_id){
        this.tut_id = tut_id;
    }
    public int getTut_id(){
        return this.tut_id;
    }
    public void setChapter_id(int chapter_id){
        this.chapter_id = chapter_id;
    }
    public int getChapter_id(){
        return this.chapter_id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setFile_path(String file_path){
        this.file_path = file_path;
    }
    public String getFile_path(){
        return this.file_path;
    }
    public void setTut_desc(String tut_desc){
        this.tut_desc = tut_desc;
    }
    public String getTut_desc(){
        return this.tut_desc;
    }
    public void setLesson(String lesson){
        this.lesson = lesson;
    }
    public String getLesson(){
        return this.lesson;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setUser_type(String user_type){
        this.user_type = user_type;
    }
    public String getUser_type(){
        return this.user_type;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
