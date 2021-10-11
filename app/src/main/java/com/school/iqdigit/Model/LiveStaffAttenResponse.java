package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveStaffAttenResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("live_attendance_class")
@Expose
private List<LiveAttendanceClas> liveAttendanceClass = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<LiveAttendanceClas> getLiveAttendanceClass() {
return liveAttendanceClass;
}

public void setLiveAttendanceClass(List<LiveAttendanceClas> liveAttendanceClass) {
this.liveAttendanceClass = liveAttendanceClass;
}

}