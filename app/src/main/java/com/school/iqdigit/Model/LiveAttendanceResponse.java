package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveAttendanceResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("live_attendance_stud")
@Expose
private List<LiveAttendanceStud> liveAttendanceStud = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<LiveAttendanceStud> getLiveAttendanceStud() {
return liveAttendanceStud;
}

public void setLiveAttendanceStud(List<LiveAttendanceStud> liveAttendanceStud) {
this.liveAttendanceStud = liveAttendanceStud;
}

}