package com.school.iqdigit.Modeldata;

public class Feereceiptlist {
    public String getFid() {
        return fid;
    }

    public String getFee_paid_date() {
        return fee_paid_date;
    }

    public String getFee_receiptno() {
        return fee_receiptno;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public String getRef_num() {
        return ref_num;
    }

    public Feereceiptlist(String fid, String fee_paid_date, String fee_receiptno, String paid_amount, String pay_mode, String ref_num) {
        this.fid = fid;
        this.fee_paid_date = fee_paid_date;
        this.fee_receiptno = fee_receiptno;
        this.paid_amount = paid_amount;
        this.pay_mode = pay_mode;
        this.ref_num = ref_num;
    }

    private String fid, fee_paid_date,fee_receiptno,paid_amount,pay_mode,ref_num;


}
