package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Attend;

import java.util.List;

public class AttendResponse {
    private boolean error;
    private List<Attend> user_attendence;

    public AttendResponse(boolean error, List<Attend> user_attendence) {
        this.error = error;
        this.user_attendence = user_attendence;
    }

    public boolean isError() {
        return error;
    }

    public List<Attend> getUser_attendence() {
        return user_attendence;
    }
}
