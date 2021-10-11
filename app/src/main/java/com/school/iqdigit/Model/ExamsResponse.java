package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Exams;

import java.util.List;

public class ExamsResponse {
    private boolean error;
    private List<Exams> examslist;

    public String getResulttype() {
        return resulttype;
    }

    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }

    private String resulttype;

    public boolean isError() {
        return error;
    }

    public List<Exams> getExamslist() {
        return examslist;
    }

    public ExamsResponse(boolean error, List<Exams> examslist,String resulttype) {
        this.error = error;
        this.examslist = examslist;
        this.resulttype = resulttype;
    }
}
