package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentPreviewResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("staff_assignment")
@Expose
private List<StaffAssignment> staffAssignment = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<StaffAssignment> getStaffAssignment() {
return staffAssignment;
}

public void setStaffAssignment(List<StaffAssignment> staffAssignment) {
this.staffAssignment = staffAssignment;
}

}