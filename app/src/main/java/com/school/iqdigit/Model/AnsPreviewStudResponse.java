package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnsPreviewStudResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("ans_preview_stud")
@Expose
private List<AnsPreviewStud> ansPreviewStud = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<AnsPreviewStud> getAnsPreviewStud() {
return ansPreviewStud;
}

public void setAnsPreviewStud(List<AnsPreviewStud> ansPreviewStud) {
this.ansPreviewStud = ansPreviewStud;
}

}