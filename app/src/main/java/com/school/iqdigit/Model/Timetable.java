package com.school.iqdigit.Model;

public class Timetable {
private String sr;
private String ptime;
private String subject;

    public Timetable(String sr, String subject, String ptime) {
        this.sr = sr;
        this.ptime = ptime;
        this.subject = subject;
    }

    public String getSr() {
return sr;
}

public void setSr(String sr) {
this.sr = sr;
}

public String getPtime() {
return ptime;
}

public void setPtime(String ptime) {
this.ptime = ptime;
}

public String getSubject() {
return subject;
}

public void setSubject(String subject) {
this.subject = subject;
}

}