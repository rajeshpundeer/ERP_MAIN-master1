package com.school.iqdigit.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.iqdigit.Adapter.AssessmentImagesAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.IcardCheckResponse;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Students;
import com.school.iqdigit.Modeldata.StudyAddResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IcardActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvInfo, text_no_available;
    private ImageView img_pic;
    private FloatingActionButton img_click_pic;
    private Uri imageUri;
    private String TAG = "IcardActivity";
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean permissiongranted;
    private Button submit;
    private File main = new File("null");
    private Intent intent;
    private String id, message, _type;
    private ImageView backbtn;
    private ProgressDialog mProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icard);
        tvInfo = findViewById(R.id.tvInfo);
        img_pic = findViewById(R.id.img_pic);
        submit = findViewById(R.id.btn_submit);
        backbtn = findViewById(R.id.backbtn);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        intent = getIntent();
        submit.setOnClickListener(this);
        text_no_available = findViewById(R.id.text_no_available);
        img_click_pic = findViewById(R.id.img_click_pic);
        img_click_pic.setOnClickListener(this);
        id = intent.getStringExtra("info");
        _type = intent.getStringExtra("type");
        Log.d(TAG, id + " id");
        if (_type.equals("student")) {
            tvInfo.setVisibility(View.VISIBLE);
            getCheckPhoto();
        }else {
            tvInfo.setVisibility(View.GONE);
        }

        checkAndRequestPermissions();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "camera services permission granted");
                    permissiongranted = true;
                } else {
                    Log.d(TAG, "camera are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showDialogOK("Service Permissions are required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                Toast.makeText(getApplicationContext(), "You need to give some mandatory permissions to upload Assessments.", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }
                                });
                    } else {
                        explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        //                            //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, IcardActivity.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                try {
                    main = new Compressor(IcardActivity.this)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(imageFiles.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(main);
                Log.d(TAG, "file_path" + imageUri);
                Glide.with(IcardActivity.this)
                        .load(imageUri)
                        .into(img_pic);
                text_no_available.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_click_pic:
                EasyImage.openChooserWithGallery(IcardActivity.this, "", 0);
                break;
            case R.id.btn_submit:
                if (!main.getPath().equals("null")) {
                    mProg.show();
                    RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"), id);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                    if(_type.equals("student")) {
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().stud_photo_update(a_subject_id, requestFile);
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse defaultResponse = response.body();
                                Log.d(TAG, " response: " + response);
                                mProg.dismiss();
                                if (defaultResponse.isErr() == false) {
                                    main = new File("");
                                    Toast.makeText(getApplicationContext(), "Images Uploaded Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                mProg.dismiss();
                            }
                        });
                    }else if(_type.equals("staff"))
                    {
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().staff_photo_update(a_subject_id, requestFile);
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse defaultResponse = response.body();
                                Log.d(TAG, " response: " + response);
                                mProg.dismiss();
                                if (defaultResponse.isErr() == false) {
                                    Toast.makeText(getApplicationContext(), "Images Uploaded Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                mProg.dismiss();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Add image", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getCheckPhoto() {
        Log.d(TAG, id);
        mProg.show();
        Call<IcardCheckResponse> call = RetrofitClient
                .getInstance().getApi().get_photo_check(id);
        call.enqueue(new Callback<IcardCheckResponse>() {
            @Override
            public void onResponse(Call<IcardCheckResponse> call, Response<IcardCheckResponse> response) {
                if (response.body().error == false) {
                    mProg.dismiss();
                    message = response.body().getPhoto_check().getPhoto_upload_msg();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tvInfo.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        tvInfo.setText(Html.fromHtml(message));
                    }
                }
            }

            @Override
            public void onFailure(Call<IcardCheckResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}