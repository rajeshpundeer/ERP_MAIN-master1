package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimetableStaff {

@SerializedName("sr")
@Expose
private String sr;
@SerializedName("ptime")
@Expose
private String ptime;
@SerializedName("subject")
@Expose
private String subject;

    public TimetableStaff(String sr, String subject, String ptime) {
        this.sr = sr;
        this.subject = subject;
        this.ptime = ptime;
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