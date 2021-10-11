package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Schooldata;

public class SchooldataResponse {
    private boolean error;
    private Schooldata school;

    public boolean isError() {
        return error;
    }

    public Schooldata getSchool() {
        return school;
    }

    public SchooldataResponse(boolean error, Schooldata school) {
        this.error = error;
        this.school = school;
    }
}
