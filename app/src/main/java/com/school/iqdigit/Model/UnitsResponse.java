package com.school.iqdigit.Model;

import java.util.List;

public class UnitsResponse {
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;


    public List<Units> getUnits() {
        return units;
    }

    private List<Units> units;
}
