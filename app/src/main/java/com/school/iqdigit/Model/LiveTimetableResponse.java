package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveTimetableResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("live_timetable")
@Expose
private List<LiveTimetable> liveTimetable = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<LiveTimetable> getLiveTimetable() {
return liveTimetable;
}

public void setLiveTimetable(List<LiveTimetable> liveTimetable) {
this.liveTimetable = liveTimetable;
}

}