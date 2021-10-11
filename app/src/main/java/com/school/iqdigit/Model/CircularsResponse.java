package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CircularsResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("circulars")
@Expose
private List<Circular> circulars = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<Circular> getCirculars() {
return circulars;
}

public void setCirculars(List<Circular> circulars) {
this.circulars = circulars;
}

}