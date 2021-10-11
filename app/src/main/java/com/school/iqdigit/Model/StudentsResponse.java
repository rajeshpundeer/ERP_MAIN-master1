package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Students;

import java.util.List;

public class StudentsResponse {
    private boolean error;
    private List<Students> students;

    public boolean isError() {
        return error;
    }

    public List<Students> getStudents() {
        return students;
    }

    public StudentsResponse(boolean error, List<Students> students) {
        this.error = error;
        this.students = students;
    }

}
