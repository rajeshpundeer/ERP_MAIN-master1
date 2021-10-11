package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimrtableStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("timetable_staff")
@Expose
private List<TimetableStaff> timetableStaff = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<TimetableStaff> getTimetableStaff() {
return timetableStaff;
}

public void setTimetableStaff(List<TimetableStaff> timetableStaff) {
this.timetableStaff = timetableStaff;
}

}