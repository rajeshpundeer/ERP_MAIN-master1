package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Feeamountlist;

import java.util.List;

public class feeamountresponse {
    private boolean error;
    private List<Feeamountlist> fee_details;

    public boolean isError() {
        return error;
    }

    public List<Feeamountlist> getFee_details() {
        return fee_details;
    }

    public feeamountresponse(boolean error, List<Feeamountlist> fee_details) {
        this.error = error;
        this.fee_details = fee_details;
    }
}
