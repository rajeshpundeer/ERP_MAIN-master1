package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class McqListResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("mcq_layout")
@Expose
private List<McqLayout> mcqLayout = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<McqLayout> getMcqLayout() {
return mcqLayout;
}

public void setMcqLayout(List<McqLayout> mcqLayout) {
this.mcqLayout = mcqLayout;
}

}