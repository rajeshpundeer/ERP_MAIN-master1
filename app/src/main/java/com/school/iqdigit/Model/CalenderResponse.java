package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Calender;

import java.util.List;

public class CalenderResponse {
    private boolean error;
    private List<Calender> calender;

    public boolean isError() {
        return error;
    }

    public List<Calender> getCalender() {
        return calender;
    }

    public CalenderResponse(boolean error, List<Calender> calender) {
        this.error = error;
        this.calender = calender;
    }
}
