package com.school.iqdigit.Modeldata;

public class Gatewaydata {
   private String gateway_provider, merchant_id , access_code, api_key, redirect_url,return_url_reg,cancel_url_reg;

    public String getGateway_provider() {
        return gateway_provider;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getAccess_code() {
        return access_code;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public String getReturn_url_reg() {
        return return_url_reg;
    }

    public String getCancel_url_reg() {
        return cancel_url_reg;
    }

    public Gatewaydata(String gateway_provider, String merchant_id, String access_code, String api_key, String redirect_url, String return_url_reg, String cancel_url_reg) {
        this.gateway_provider = gateway_provider;
        this.merchant_id = merchant_id;
        this.access_code = access_code;
        this.api_key = api_key;
        this.redirect_url = redirect_url;
        this.return_url_reg = return_url_reg;
        this.cancel_url_reg = cancel_url_reg;
    }
}
