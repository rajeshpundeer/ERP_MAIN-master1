package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingFeeResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("pendingfee_alert")
@Expose
private String pendingfeeAlert;
@SerializedName("fee_force_pay")
@Expose
private String feeForcePay;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public String getPendingfeeAlert() {
return pendingfeeAlert;
}

public void setPendingfeeAlert(String pendingfeeAlert) {
this.pendingfeeAlert = pendingfeeAlert;
}

public String getFeeForcePay() {
return feeForcePay;
}

public void setFeeForcePay(String feeForcePay) {
this.feeForcePay = feeForcePay;
}

}