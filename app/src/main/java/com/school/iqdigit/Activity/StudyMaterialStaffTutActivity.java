package com.school.iqdigit.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.BookletsAdapter;
import com.school.iqdigit.Adapter.StudyImageAdapter;
import com.school.iqdigit.Adapter.TutorialAdapter;
import com.school.iqdigit.Adapter.VideoAdapter;
import com.school.iqdigit.Api.RetrofitClient;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudyMaterialStaffTutActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitleStudy;
    private String chaptername, chapterid;
    private ImageView imgBack;
    private RecyclerView rvTutorial, rvBooklet, rvvideo, rv_studyimages;
    private ProgressDialog mProg;
    private BookletsAdapter bookletsAdapter;
    private TutorialAdapter tutorialAdapter;
    private VideoAdapter videoAdapter;
    private StudyImageAdapter studyImageAdapter;
    private List<Booklets> bookletsList = new ArrayList<>();
    private List<Tutorials> tutorialsList = new ArrayList<>();
    private List<Media> mediaList = new ArrayList<>();
    private List<Image> imagesList = new ArrayList<>();
    private String TAG = "StudyMaterialTutorialActivity";
    private FloatingActionButton fabTutorial, fabBooklet, fabImages, fabVideo;
    private String subject_id = "", type = "";
    private Intent mainintent;
    private TextView navigation_tutorial, navigation_booklet, navigation_images, navigation_Video, acti_title;
    private LinearLayout view_tutorial, view_booklet, view_images, view_video,noavailable;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_staff_tut);
        Bundle bundle = getIntent().getExtras();
        chapterid = bundle.getString("chapterid");
        chaptername = bundle.getString("chaptername");
        mainintent = getIntent();
        subject_id = mainintent.getStringExtra("subject_id");
        type = mainintent.getStringExtra("type");
        Log.d(TAG, chapterid + " chapterid");
        navigation_tutorial = findViewById(R.id.navigation_tutorial);
        navigation_tutorial.setOnClickListener(this);
        navigation_booklet = findViewById(R.id.navigation_booklet);
        navigation_booklet.setOnClickListener(this);
        navigation_images = findViewById(R.id.navigation_images);
        navigation_images.setOnClickListener(this);
        navigation_Video = findViewById(R.id.navigation_Video);
        navigation_Video.setOnClickListener(this);
        view_booklet = findViewById(R.id.view_booklet);
        view_images = findViewById(R.id.view_images);
        view_tutorial = findViewById(R.id.view_tutorial);
        view_video = findViewById(R.id.view_video);
        tvTitleStudy = findViewById(R.id.tvTitleStudy);
        tvTitleStudy.setText(chaptername);
        rvBooklet = findViewById(R.id.rvBooklet);
        rvTutorial = findViewById(R.id.rvTutorial);
        rvvideo = findViewById(R.id.rvvideo);
        rv_studyimages = findViewById(R.id.rv_studyimages);
        imgBack = findViewById(R.id.backbtn);
        imgBack.setOnClickListener(this);
        fabTutorial = findViewById(R.id.fabTutorial);
        fabTutorial.setOnClickListener(this);
        fabBooklet = findViewById(R.id.fabBooklet);
        fabBooklet.setOnClickListener(this);
        fabImages = findViewById(R.id.fabImages);
        fabImages.setOnClickListener(this);
        fabVideo = findViewById(R.id.fabVideo);
        fabVideo.setOnClickListener(this);
        acti_title = findViewById(R.id.acti_title);
        noavailable = findViewById(R.id.noavailable);

        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        if (type.equals("")) {
            getTutorials();
            getBooklets();
            getImages();
            getMedia();
        }

        if (type.equals("tutorial")) {
            getTutorials();
            view_tutorial.setVisibility(View.VISIBLE);
            view_booklet.setVisibility(View.GONE);
            view_images.setVisibility(View.GONE);
            view_video.setVisibility(View.GONE);
            rvTutorial.setVisibility(View.VISIBLE);
            rvBooklet.setVisibility(View.GONE);
            rvvideo.setVisibility(View.GONE);
            rv_studyimages.setVisibility(View.GONE);
            fabTutorial.setVisibility(View.VISIBLE);
            fabBooklet.setVisibility(View.GONE);
            fabImages.setVisibility(View.GONE);
            fabVideo.setVisibility(View.GONE);
        }
        if (type.equals("pdf")) {
            getBooklets();
            view_tutorial.setVisibility(View.GONE);
            view_booklet.setVisibility(View.VISIBLE);
            view_images.setVisibility(View.GONE);
            view_video.setVisibility(View.GONE);
            rvTutorial.setVisibility(View.GONE);
            rvBooklet.setVisibility(View.VISIBLE);
            rvvideo.setVisibility(View.GONE);
            rv_studyimages.setVisibility(View.GONE);
            fabTutorial.setVisibility(View.GONE);
            fabBooklet.setVisibility(View.VISIBLE);
            fabImages.setVisibility(View.GONE);
            fabVideo.setVisibility(View.GONE);

        }

        if (type.equals("video")) {
            getMedia();
            view_tutorial.setVisibility(View.GONE);
            view_booklet.setVisibility(View.GONE);
            view_images.setVisibility(View.GONE);
            view_video.setVisibility(View.VISIBLE);
            rvTutorial.setVisibility(View.GONE);
            rvBooklet.setVisibility(View.GONE);
            rvvideo.setVisibility(View.VISIBLE);
            rv_studyimages.setVisibility(View.GONE);
            fabTutorial.setVisibility(View.GONE);
            fabBooklet.setVisibility(View.GONE);
            fabImages.setVisibility(View.GONE);
            fabVideo.setVisibility(View.VISIBLE);
        }

        if (type.equals("image")) {
            getImages();
            view_tutorial.setVisibility(View.GONE);
            view_booklet.setVisibility(View.GONE);
            view_images.setVisibility(View.VISIBLE);
            view_video.setVisibility(View.GONE);
            rvTutorial.setVisibility(View.GONE);
            rvBooklet.setVisibility(View.GONE);
            rvvideo.setVisibility(View.GONE);
            rv_studyimages.setVisibility(View.VISIBLE);
            fabTutorial.setVisibility(View.GONE);
            fabBooklet.setVisibility(View.GONE);
            fabImages.setVisibility(View.VISIBLE);
            fabVideo.setVisibility(View.GONE);
        }

    }

    private void getImages() {
        if (InternetCheck.isInternetOn(StudyMaterialStaffTutActivity.this) == true) {
            mProg.show();
            Call<ImageRespose> call2 = RetrofitClient.getInstance().getApi().getimage(chapterid);

            call2.enqueue(new Callback<ImageRespose>() {
                @Override
                public void onResponse(Call<ImageRespose> call, Response<ImageRespose> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            imagesList = response.body().getImage();
                            if (imagesList.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                rv_studyimages.setLayoutManager(new LinearLayoutManager(StudyMaterialStaffTutActivity.this));
                                studyImageAdapter = new StudyImageAdapter(StudyMaterialStaffTutActivity.this, imagesList, "staff", chapterid, chaptername, subject_id);
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
                    noavailable.setVisibility(View.VISIBLE);
                    acti_title.setText("No Images Available");
                  //  Toast.makeText(getApplicationContext(), "No Images Available", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getBooklets() {
        if (InternetCheck.isInternetOn(StudyMaterialStaffTutActivity.this) == true) {
            mProg.show();
            Call<BookletResponse> call2 = RetrofitClient.getInstance().getApi().getbooklets(chapterid);

            call2.enqueue(new Callback<BookletResponse>() {
                @Override
                public void onResponse(Call<BookletResponse> call, Response<BookletResponse> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            bookletsList = response.body().getBooklets();
                            if (bookletsList.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                rvBooklet.setLayoutManager(new LinearLayoutManager(StudyMaterialStaffTutActivity.this));
                                bookletsAdapter = new BookletsAdapter(StudyMaterialStaffTutActivity.this, bookletsList, "staff", chapterid, chaptername, subject_id);
                                rvBooklet.setAdapter(bookletsAdapter);
                            }else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Booklets Available");
                            }
                        }
                    } else {
                        mProg.dismiss();
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Booklets Available");
                       // Toast.makeText(getApplicationContext(), "No Booklets Available", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<BookletResponse> call, Throwable t) {
                    mProg.dismiss();
                    noavailable.setVisibility(View.VISIBLE);
                    acti_title.setText("No Booklets Available");
                    //Toast.makeText(getApplicationContext(), "No Booklets Available", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getTutorials() {
        if (InternetCheck.isInternetOn(StudyMaterialStaffTutActivity.this) == true) {
            mProg.show();
            Call<TutorialResponse> call2 = RetrofitClient.getInstance().getApi().gettutorials(chapterid);

            call2.enqueue(new Callback<TutorialResponse>() {
                @Override
                public void onResponse(Call<TutorialResponse> call, Response<TutorialResponse> response) {
                    mProg.dismiss();

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            tutorialsList = response.body().getTutorials();
                            if (tutorialsList.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                rvTutorial.setLayoutManager(new LinearLayoutManager(StudyMaterialStaffTutActivity.this));
                                tutorialAdapter = new TutorialAdapter(StudyMaterialStaffTutActivity.this, tutorialsList, "staff", chapterid, chaptername, subject_id);
                                rvTutorial.setAdapter(tutorialAdapter);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Tutorials Available");
                                //Toast.makeText(getApplicationContext(), "No Tutorials Available", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<TutorialResponse> call, Throwable t) {
                    mProg.dismiss();
                    noavailable.setVisibility(View.VISIBLE);
                    acti_title.setText("No Tutorials Available");                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getMedia() {
        if (InternetCheck.isInternetOn(StudyMaterialStaffTutActivity.this) == true) {
            mProg.show();
            Call<MediaRespose> call2 = RetrofitClient.getInstance().getApi().getmedia(chapterid);

            call2.enqueue(new Callback<MediaRespose>() {
                @Override
                public void onResponse(Call<MediaRespose> call, Response<MediaRespose> response) {

                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            mediaList = response.body().getMedia();
                            if (mediaList.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                rvvideo.setLayoutManager(new LinearLayoutManager(StudyMaterialStaffTutActivity.this));
                                videoAdapter = new VideoAdapter(StudyMaterialStaffTutActivity.this, mediaList, "staff", chapterid, chaptername, subject_id);
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
                    noavailable.setVisibility(View.VISIBLE);
                    acti_title.setText("No Video Available");
                    //Toast.makeText(getApplicationContext(), "No Video Available", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                onBackPressed();
                break;
            case R.id.fabTutorial:
                Intent intent = new Intent(StudyMaterialStaffTutActivity.this, AddTutorialActivity.class);
                intent.putExtra("chapterid", chapterid);
                intent.putExtra("subject_id", subject_id);
                intent.putExtra("chapter_name", chaptername);
                intent.putExtra("title", "");
                intent.putExtra("lesson", "");
                intent.putExtra("action", "add");
                intent.putExtra("running_id", "");
                startActivity(intent);
                finish();
                break;
            case R.id.fabBooklet:
                Intent intent2 = new Intent(StudyMaterialStaffTutActivity.this, AddPdfActivity.class);
                intent2.putExtra("chapterid", chapterid);
                intent2.putExtra("subject_id", subject_id);
                intent2.putExtra("chapter_name", chaptername);
                intent2.putExtra("title", "");
                intent2.putExtra("lesson", "");
                intent2.putExtra("action", "add");
                intent2.putExtra("running_id", "");
                startActivity(intent2);
                finish();
                break;
            case R.id.fabImages:
                Intent intent1 = new Intent(StudyMaterialStaffTutActivity.this, AddBookletActivity.class);
                intent1.putExtra("chapterid", chapterid);
                intent1.putExtra("subject_id", subject_id);
                intent1.putExtra("chapter_name", chaptername);
                intent1.putExtra("title", "");
                intent1.putExtra("lesson", "");
                intent1.putExtra("action", "add");
                intent1.putExtra("running_id", "");
                startActivity(intent1);
                finish();
                break;
            case R.id.fabVideo:
                Intent intent3 = new Intent(StudyMaterialStaffTutActivity.this, AddVideoActivity.class);
                intent3.putExtra("chapterid", chapterid);
                intent3.putExtra("subject_id", subject_id);
                intent3.putExtra("chapter_name", chaptername);
                intent3.putExtra("title", "");
                intent3.putExtra("lesson", "");
                intent3.putExtra("action", "add");
                intent3.putExtra("running_id", "");
                startActivity(intent3);
                finish();
                break;
            case R.id.navigation_tutorial:
                getTutorials();
                view_tutorial.setVisibility(View.VISIBLE);
                view_booklet.setVisibility(View.GONE);
                view_images.setVisibility(View.GONE);
                view_video.setVisibility(View.GONE);
                rvTutorial.setVisibility(View.VISIBLE);
                rvBooklet.setVisibility(View.GONE);
                rvvideo.setVisibility(View.GONE);
                rv_studyimages.setVisibility(View.GONE);
                fabTutorial.setVisibility(View.VISIBLE);
                fabBooklet.setVisibility(View.GONE);
                fabImages.setVisibility(View.GONE);
                fabVideo.setVisibility(View.GONE);
                break;
            case R.id.navigation_booklet:
                getBooklets();
                view_tutorial.setVisibility(View.GONE);
                view_booklet.setVisibility(View.VISIBLE);
                view_images.setVisibility(View.GONE);
                view_video.setVisibility(View.GONE);
                rvTutorial.setVisibility(View.GONE);
                rvBooklet.setVisibility(View.VISIBLE);
                rvvideo.setVisibility(View.GONE);
                rv_studyimages.setVisibility(View.GONE);
                fabTutorial.setVisibility(View.GONE);
                fabBooklet.setVisibility(View.VISIBLE);
                fabImages.setVisibility(View.GONE);
                fabVideo.setVisibility(View.GONE);
                break;
            case R.id.navigation_Video:
                getMedia();
                view_tutorial.setVisibility(View.GONE);
                view_booklet.setVisibility(View.GONE);
                view_images.setVisibility(View.GONE);
                view_video.setVisibility(View.VISIBLE);
                rvTutorial.setVisibility(View.GONE);
                rvBooklet.setVisibility(View.GONE);
                rvvideo.setVisibility(View.VISIBLE);
                rv_studyimages.setVisibility(View.GONE);
                fabTutorial.setVisibility(View.GONE);
                fabBooklet.setVisibility(View.GONE);
                fabImages.setVisibility(View.GONE);
                fabVideo.setVisibility(View.VISIBLE);
                break;

            case R.id.navigation_images:
                getImages();
                view_tutorial.setVisibility(View.GONE);
                view_booklet.setVisibility(View.GONE);
                view_images.setVisibility(View.VISIBLE);
                view_video.setVisibility(View.GONE);
                rvTutorial.setVisibility(View.GONE);
                rvBooklet.setVisibility(View.GONE);
                rvvideo.setVisibility(View.GONE);
                rv_studyimages.setVisibility(View.VISIBLE);
                fabTutorial.setVisibility(View.GONE);
                fabBooklet.setVisibility(View.GONE);
                fabImages.setVisibility(View.VISIBLE);
                fabVideo.setVisibility(View.GONE);
                break;
        }
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
                        if (InternetCheck.isInternetOn(StudyMaterialStaffTutActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}
