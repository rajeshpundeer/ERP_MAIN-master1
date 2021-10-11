package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Feelist;

import java.util.List;

public class feelistresponse {
    private boolean error;

    public String getDefault_selection() {
        return default_selection;
    }

    public void setDefault_selection(String default_selection) {
        this.default_selection = default_selection;
    }

    private String default_selection;
    private List<Feelist> user_fee_struc;

    public boolean isError() {
        return error;
    }

    public List<Feelist> getUser_fee_struc() {
        return user_fee_struc;
    }

    public feelistresponse(boolean error, List<Feelist> user_fee_struc) {
        this.error = error;
        this.user_fee_struc = user_fee_struc;
    }
}
