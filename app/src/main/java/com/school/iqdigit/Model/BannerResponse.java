package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Banner;

import java.util.List;

public class BannerResponse {
    private  boolean error;

    public boolean isError() {
        return error;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public BannerResponse(boolean error, List<Banner> banner) {
        this.error = error;
        this.banner = banner;
    }
    private List<Banner> banner;
}
