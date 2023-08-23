package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStaffTimeResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("live_timetable_staff")
@Expose
private List<LiveTimetableStaff> liveTimetableStaff = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<LiveTimetableStaff> getLiveTimetableStaff() {
return liveTimetableStaff;
}

public void setLiveTimetableStaff(List<LiveTimetableStaff> liveTimetableStaff) {
this.liveTimetableStaff = liveTimetableStaff;
}

}