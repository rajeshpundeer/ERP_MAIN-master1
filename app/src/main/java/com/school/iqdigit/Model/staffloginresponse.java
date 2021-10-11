package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Staff;

public class staffloginresponse {
    private boolean error;
    private String message;
    private Staff staff;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Staff getStaff() {
        return staff;
    }

    public staffloginresponse(boolean error, String message, Staff staff) {
        this.error = error;
        this.message = message;
        this.staff = staff;
    }
}
