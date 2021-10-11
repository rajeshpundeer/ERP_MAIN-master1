package com.school.iqdigit.Model;

public class feedatamodel {
    private String fid, orderid, amount;

    public feedatamodel(String fid, String orderid, String amount) {
        this.fid = fid;
        this.orderid = orderid;
        this.amount = amount;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
