package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnsPreviewStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("ans_preview_staff")
@Expose
private List<AnsPreviewStaff> ansPreviewStaff = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<AnsPreviewStaff> getAnsPreviewStaff() {
return ansPreviewStaff;
}

public void setAnsPreviewStaff(List<AnsPreviewStaff> ansPreviewStaff) {
this.ansPreviewStaff = ansPreviewStaff;
}

}