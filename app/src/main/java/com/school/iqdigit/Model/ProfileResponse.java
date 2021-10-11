package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Userprofile;

public class ProfileResponse {
    private Userprofile userprofile;
    private boolean error;

    public ProfileResponse(Userprofile userprofile, boolean error) {
        this.userprofile = userprofile;
        this.error = error;
    }

    public Userprofile getUserprofile() {
        return userprofile;
    }

    public boolean isError() {
        return error;
    }
}
