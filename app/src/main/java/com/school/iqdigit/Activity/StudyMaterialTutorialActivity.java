package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AchievementAdapter;
import com.school.iqdigit.Adapter.BookletsAdapter;
import com.school.iqdigit.Adapter.StudyImageAdapter;
import com.school.iqdigit.Adapter.TutorialAdapter;
import com.school.iqdigit.Adapter.VideoAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.BookletResponse;
import com.school.iqdigit.Model.Booklets;
import com.school.iqdigit.Model.Image;
import com.school.iqdigit.Model.ImageRespose;
import com.school.iqdigit.Model.Media;
import com.school.iqdigit.Model.MediaRespose;
import com.school.iqdigit.Model.TutorialResponse;
import com.school.iqdigit.Model.Tutorials;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudyMaterialTutorialActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitleStudy, acti_title;
    private String chaptername, chapterid;
    private ImageView imgBack;
    private BottomNavigationView navigationView;
    private RecyclerView rvTutorial, rvBooklet, rvvideo ,rv_studyimages;
    private ProgressDialog mProg;
    private BookletsAdapter bookletsAdapter;
    private TutorialAdapter tutorialAdapter;
    private VideoAdapter videoAdapter;
    private StudyImageAdapter studyImageAdapter;
    private List<Booklets> bookletsList = new ArrayList<>();
    private List<Tutorials> tutorialsList = new ArrayList<>();
    private List<Media> mediaList = new ArrayList<>();
    private List<Image> imagesList = new ArrayList<>();
    private LinearLayout noavailable;

    private String TAG = "StudyMaterialTutorialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_tutorial);
        Bundle bundle = getIntent().getExtras();
        chapterid = bundle.getString("chapterid");
        chaptername = bundle.getString("chaptername");
        tvTitleStudy = findViewById(R.id.tvTitleStudy);
        tvTitleStudy.setText(chaptername);
        rvBooklet = findViewById(R.id.rvBooklet);
        rvTutorial = findViewById(R.id.rvTutorial);
        rvvideo = findViewById(R.id.rvvideo);
        rv_studyimages = findViewById(R.id.rv_studyimages);
        imgBack = findViewById(R.id.imgbackstudy);
        imgBack.setOnClickListener(this);
        navigationView = findViewById(R.id.navigationStudy);
        acti_title = findViewById(R.id.acti_title);
        noavailable = findViewById(R.id.noavailable);
        setTabview();
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        getTutorials();
        getBooklets();
        getImages();
        getMedia();
    }

    private void getImages() {
        if(InternetCheck.isInternetOn(StudyMaterialTutorialActivity.this) == true) {
            mProg.show();
            Call<ImageRespose> call2 = RetrofitClient.getInstance().getApi().getimage(chapterid);

            call2.enqueue(new Callback<ImageRespose>() {
                @Override
                public void onResponse(Call<ImageRespose> call, Response<ImageRespose> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            imagesList = response.body().getImage();
                            if(imagesList.size()>0) {
                                noavailable.setVisibility(View.GONE);
                                rv_studyimages.setLayoutManager(new LinearLayoutManager(StudyMaterialTutorialActivity.this));
                                studyImageAdapter = new StudyImageAdapter(StudyMaterialTutorialActivity.this, imagesList,"student","","","");
                                rv_studyimages.setAdapter(studyImageAdapter);
                            }else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Images Available");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ImageRespose> call, Throwable t) {
                    mProg.dismiss();
                }
            });
        }else
        {
            showsnackbar();
        }
    }


    private void getBooklets() {
        if(InternetCheck.isInternetOn(StudyMaterialTutorialActivity.this) == true) {
            mProg.show();
            Call<BookletResponse> call2 = RetrofitClient.getInstance().getApi().getbooklets(chapterid);

            call2.enqueue(new Callback<BookletResponse>() {
                @Override
                public void onResponse(Call<BookletResponse> call, Response<BookletResponse> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            bookletsList = response.body().getBooklets();
                            if(bookletsList.size()>0) {
                                noavailable.setVisibility(View.GONE);
                                rvBooklet.setLayoutManager(new LinearLayoutManager(StudyMaterialTutorialActivity.this));
                                bookletsAdapter = new BookletsAdapter(StudyMaterialTutorialActivity.this, bookletsList,"student","","","");
                                rvBooklet.setAdapter(bookletsAdapter);
                            }else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No PDF(S) Available");
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<BookletResponse> call, Throwable t) {
                    mProg.dismiss();

                }
            });
        }else
        {
            showsnackbar();
        }
    }

    private void getTutorials() {
        if(InternetCheck.isInternetOn(StudyMaterialTutorialActivity.this) == true) {
            mProg.show();
            Call<TutorialResponse> call2 = RetrofitClient.getInstance().getApi().gettutorials(chapterid);

            call2.enqueue(new Callback<TutorialResponse>() {
                @Override
                public void onResponse(Call<TutorialResponse> call, Response<TutorialResponse> response) {
                    mProg.dismiss();
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            tutorialsList = response.body().getTutorials();
                            if(tutorialsList.size()>0) {
                                noavailable.setVisibility(View.GONE);
                                rvTutorial.setLayoutManager(new LinearLayoutManager(StudyMaterialTutorialActivity.this));
                                tutorialAdapter = new TutorialAdapter(StudyMaterialTutorialActivity.this, tutorialsList,"student","","","");
                                rvTutorial.setAdapter(tutorialAdapter);
                            }else
                            {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Tutorials Available");
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<TutorialResponse> call, Throwable t) {
                    mProg.dismiss();

                }
            });
        }else
        {
            showsnackbar();
        }
    }
    private void getMedia() {
        if(InternetCheck.isInternetOn(StudyMaterialTutorialActivity.this) == true) {
            mProg.show();
            Call<MediaRespose> call2 = RetrofitClient.getInstance().getApi().getmedia(chapterid);

            call2.enqueue(new Callback<MediaRespose>() {
                @Override
                public void onResponse(Call<MediaRespose> call, Response<MediaRespose> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            mediaList = response.body().getMedia();
                            if(mediaList.size()>0) {
                                noavailable.setVisibility(View.GONE);
                                rvvideo.setLayoutManager(new LinearLayoutManager(StudyMaterialTutorialActivity.this));
                                videoAdapter = new VideoAdapter(StudyMaterialTutorialActivity.this, mediaList,"student","","","");
                                rvvideo.setAdapter(videoAdapter);
                            }else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Video Available");
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<MediaRespose> call, Throwable t) {
                    mProg.dismiss();
                }
            });
        }else
        {
            showsnackbar();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbackstudy: {
                onBackPressed();
            }
        }
    }


    private void setTabview() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_tutorial:
                        getTutorials();
                        rvTutorial.setVisibility(View.VISIBLE);
                        rvBooklet.setVisibility(View.GONE);
                        rvvideo.setVisibility(View.GONE);
                        rv_studyimages.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_booklet:
                        getBooklets();
                        rvTutorial.setVisibility(View.GONE);
                        rvBooklet.setVisibility(View.VISIBLE);
                        rvvideo.setVisibility(View.GONE);
                        rv_studyimages.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_Video:
                        getMedia();
                        rvTutorial.setVisibility(View.GONE);
                        rvBooklet.setVisibility(View.GONE);
                        rvvideo.setVisibility(View.VISIBLE);
                        rv_studyimages.setVisibility(View.GONE);
                        return true;

                    case R.id.navigation_images:
                        getImages();
                        rvTutorial.setVisibility(View.GONE);
                        rvBooklet.setVisibility(View.GONE);
                        rvvideo.setVisibility(View.GONE);
                        rv_studyimages.setVisibility(View.VISIBLE);
                        return true;
                }
                return true;
            }
        });
    }
    private void showsnackbar() {
        if (mProg.isShowing()) {
            mProg.dismiss();
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(StudyMaterialTutorialActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}
