package com.school.iqdigit.Modeldata;

public class Feeamountlist {
    private String fee_type;
    private String fee_month;
    private String fee_id;
    private String fee_amount;

    public String getTotal_fee_amount() {
        return total_fee_amount;
    }

    public Feeamountlist(String total_fee_amount) {
        this.total_fee_amount = total_fee_amount;
    }

    private String total_fee_amount;

    public String getFee_type() {
        return fee_type;
    }

    public String getFee_month() {
        return fee_month;
    }

    public String getFee_id() {
        return fee_id;
    }

    public String getFee_amount() {
        return fee_amount;
    }

    public Feeamountlist(String fee_type, String fee_month, String fee_id, String fee_amount) {
        this.fee_type = fee_type;
        this.fee_month = fee_month;
        this.fee_id = fee_id;
        this.fee_amount = fee_amount;
    }
}
