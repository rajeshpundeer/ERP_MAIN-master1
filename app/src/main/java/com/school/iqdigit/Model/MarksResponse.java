package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Markslist;

import java.util.List;

public class MarksResponse {
    private boolean error;

    public String getPercent() {
        return percent;
    }

    private String percent;

    public String getRank() {
        return rank;
    }

    private String rank;
    private List<Markslist> examsmlist;

    public boolean isError() {
        return error;
    }

    public List<Markslist> getExamsmlist() {
        return examsmlist;
    }

    public MarksResponse(boolean error, List<Markslist> examsmlist,String percent) {
        this.error = error;
        this.examsmlist = examsmlist;
        this.percent = percent;
    }
}
