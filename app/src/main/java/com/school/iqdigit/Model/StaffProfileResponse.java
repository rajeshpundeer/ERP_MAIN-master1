package com.school.iqdigit.Model;

public class StaffProfileResponse {
    public boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Staffprofile getStaffprofile() {
        return staffprofile;
    }

    public void setStaffprofile(Staffprofile staffprofile) {
        this.staffprofile = staffprofile;
    }

    public Staffprofile staffprofile;
}