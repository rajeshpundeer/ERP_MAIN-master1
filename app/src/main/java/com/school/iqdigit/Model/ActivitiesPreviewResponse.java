package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivitiesPreviewResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("staff_activities")
@Expose
private List<StaffActivity> staffActivities = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<StaffActivity> getStaffActivities() {
return staffActivities;
}

public void setStaffActivities(List<StaffActivity> staffActivities) {
this.staffActivities = staffActivities;
}

}