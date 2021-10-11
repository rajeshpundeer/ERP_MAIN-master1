package com.school.iqdigit.Modeldata;

import java.util.List;

public class StudentsIcardResponse {
    public boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<StudentsPhoto> getStudents_photo() {
        return students_photo;
    }

    public void setStudents_photo(List<StudentsPhoto> students_photo) {
        this.students_photo = students_photo;
    }

    public List<StudentsPhoto> students_photo;
}