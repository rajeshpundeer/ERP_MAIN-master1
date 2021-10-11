package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Subjects;

import java.util.List;

public class SubjectResponse {
    private boolean error;
    private List<Subjects> subjects;

    public boolean isError() {
        return error;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }

    public SubjectResponse(boolean error, List<Subjects> subjects) {
        this.error = error;
        this.subjects = subjects;
    }
}
