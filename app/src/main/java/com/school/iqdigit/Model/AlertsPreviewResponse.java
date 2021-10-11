package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertsPreviewResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("staff_alerts")
@Expose
private List<StaffAlert> staffAlerts = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<StaffAlert> getStaffAlerts() {
return staffAlerts;
}

public void setStaffAlerts(List<StaffAlert> staffAlerts) {
this.staffAlerts = staffAlerts;
}

}