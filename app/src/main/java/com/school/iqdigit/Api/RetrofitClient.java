package com.school.iqdigit.Api;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.school.iqdigit.BuildConfig;

        import java.sql.Time;
        import java.util.concurrent.TimeUnit;

        import okhttp3.Cache;
        import okhttp3.ConnectionPool;
        import okhttp3.OkHttpClient;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL2 = BuildConfig.BASE_UR ;
    private static String BASE_URL = BASE_URL2+"app/public/" ;
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200,TimeUnit.SECONDS)
            .writeTimeout(200,TimeUnit.SECONDS).build();
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
