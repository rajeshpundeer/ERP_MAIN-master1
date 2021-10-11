package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AlbumnAdapter;
import com.school.iqdigit.Adapter.BirthdayAdapter;
import com.school.iqdigit.Adapter.GalleryAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AlbumnRespose;
import com.school.iqdigit.Model.BirthdaysResponse;
import com.school.iqdigit.Model.GetPhoto;
import com.school.iqdigit.Model.Photoalbum;
import com.school.iqdigit.Model.PhotosRespose;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.GalleryClicked;
import com.school.iqdigit.interfaces.GetClikedIalbumPath;
import com.school.iqdigit.utility.InternetCheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends AppCompatActivity implements GetClikedIalbumPath, GalleryClicked {
    private RecyclerView vericalsRecyclerView;
    private RecyclerView galleryRecyclerView;
    private RelativeLayout progressbar;
    private ProgressDialog mProg;
    private TextView tvGallery;
    private LinearLayout noGallery;
    private ImageView backbtn;
    private TextView toolbar_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        vericalsRecyclerView = findViewById(R.id.vericalsRecyclerView);
        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
        tvGallery = findViewById(R.id.tvGallery);
        progressbar = findViewById(R.id.progressbar);
        backbtn = findViewById(R.id.backbtn);
        noGallery = findViewById(R.id.noGallery);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        getAlbum();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAlbum() {
        mProg.create();
        if (InternetCheck.isInternetOn(GalleryActivity.this) == true) {
            Call<AlbumnRespose> call = RetrofitClient
                    .getInstance().getApi().getalbumns();
            call.enqueue(new Callback<AlbumnRespose>() {
                @Override
                public void onResponse(Call<AlbumnRespose> call, Response<AlbumnRespose> response) {
                    if(response.isSuccessful() && response.body().getPhotoalbum().size() > 0){
                        List<Photoalbum> photoalbum;
                        tvGallery.setVisibility(View.VISIBLE);
                        noGallery.setVisibility(View.GONE);
                        photoalbum = response.body().getPhotoalbum();
                        vericalsRecyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        vericalsRecyclerView.setAdapter(new AlbumnAdapter(tvGallery,photoalbum, GalleryActivity.this,GalleryActivity.this));
                        getGallery(String.valueOf(photoalbum.get(0).getId()));
                        tvGallery.setText(photoalbum.get(0).getName());
                    }else
                    {
                        tvGallery.setVisibility(View.GONE);
                        noGallery.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<AlbumnRespose> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getGallery(String id) {
        if (InternetCheck.isInternetOn(GalleryActivity.this) == true) {
            Call<PhotosRespose> call = RetrofitClient
                    .getInstance().getApi().get_photos(id);
            call.enqueue(new Callback<PhotosRespose>() {
                @Override
                public void onResponse(Call<PhotosRespose> call, Response<PhotosRespose> response) {

                    if(response.isSuccessful() && response.body().getGetPhotos().size() > 0){
                        galleryRecyclerView.setVisibility(View.VISIBLE);
                        noGallery.setVisibility(View.GONE);
                        List<GetPhoto> getPhotos;
                        getPhotos = response.body().getGetPhotos();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        galleryRecyclerView.setLayoutManager(gridLayoutManager);
                        galleryRecyclerView.setAdapter(new GalleryAdapter(progressbar,getPhotos, GalleryActivity.this,GalleryActivity.this));
                        mProg.dismiss();
                    }else {
                        galleryRecyclerView.setVisibility(View.GONE);
                        noGallery.setVisibility(View.VISIBLE);
                        mProg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<PhotosRespose> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
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
                        if (InternetCheck.isInternetOn(GalleryActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void getId(String albumnid) {
        getGallery(albumnid);
    }

    @Override
    public void getId(Integer photoid, List<GetPhoto> getPhotos) {
        List<String> image = new ArrayList<>();

        for (int i = 0; i< getPhotos.size();i++)
        {
            image.add(getPhotos.get(i).getImg());
        }
        Intent intent = new Intent(GalleryActivity.this, GalleryViewActivity.class);
        intent.putStringArrayListExtra("list", (ArrayList<String>) image);
        intent.putExtra("position",String.valueOf(photoid));
        startActivity(intent);
    }
}