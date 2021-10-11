package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BirthdaysResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("birthdays")
@Expose
private List<Birthday> birthdays = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<Birthday> getBirthdays() {
return birthdays;
}

public void setBirthdays(List<Birthday> birthdays) {
this.birthdays = birthdays;
}

}