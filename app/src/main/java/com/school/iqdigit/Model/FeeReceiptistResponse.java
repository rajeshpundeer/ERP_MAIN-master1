package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Feereceiptlist;

import java.util.List;

public class FeeReceiptistResponse {
    private boolean error;
    private List<Feereceiptlist> allreceipt;

    public boolean isError() {
        return error;
    }

    public List<Feereceiptlist> getAllreceipt() {
        return allreceipt;
    }

    public FeeReceiptistResponse(boolean error, List<Feereceiptlist> allreceipt) {
        this.error = error;
        this.allreceipt = allreceipt;
    }
}
