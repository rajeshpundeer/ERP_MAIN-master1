package com.school.iqdigit.Model;

import java.util.List;

public class Timetableresponse {

private Boolean error;
private List<Timetable> timetable = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<Timetable> getTimetable() {
return timetable;
}

public void setTimetable(List<Timetable> timetable) {
this.timetable = timetable;
}

}