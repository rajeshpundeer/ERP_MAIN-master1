package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumnRespose {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("photoalbum")
@Expose
private List<Photoalbum> photoalbum = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<Photoalbum> getPhotoalbum() {
return photoalbum;
}

public void setPhotoalbum(List<Photoalbum> photoalbum) {
this.photoalbum = photoalbum;
}

}