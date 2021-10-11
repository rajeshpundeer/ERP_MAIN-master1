package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotosRespose {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("get_photos")
@Expose
private List<GetPhoto> getPhotos = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<GetPhoto> getGetPhotos() {
return getPhotos;
}

public void setGetPhotos(List<GetPhoto> getPhotos) {
this.getPhotos = getPhotos;
}

}