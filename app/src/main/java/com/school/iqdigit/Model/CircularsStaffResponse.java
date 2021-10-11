package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CircularsStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("circulars_staff")
@Expose
private List<CircularsStaff> circularsStaff = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<CircularsStaff> getCircularsStaff() {
return circularsStaff;
}

public void setCircularsStaff(List<CircularsStaff> circularsStaff) {
this.circularsStaff = circularsStaff;
}

}