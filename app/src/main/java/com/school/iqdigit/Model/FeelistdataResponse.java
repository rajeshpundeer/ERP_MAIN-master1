package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Feelistdata;

import java.util.List;

public class FeelistdataResponse {
    private boolean error;
    private List<Feelistdata> receipt;

    public boolean isError() {
        return error;
    }

    public List<Feelistdata> getReceipt() {
        return receipt;
    }

    public FeelistdataResponse(boolean error, List<Feelistdata> receipt) {
        this.error = error;
        this.receipt = receipt;
    }
}
