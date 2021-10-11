package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Classes;

import java.util.List;

public class ClassResponse {
    private boolean error;
    private List<Classes> classes;

    public boolean isError() {
        return error;
    }

    public List<Classes> getClasses() {
        return classes;
    }

    public ClassResponse(boolean error, List<Classes> classes) {
        this.error = error;
        this.classes = classes;
    }
}
