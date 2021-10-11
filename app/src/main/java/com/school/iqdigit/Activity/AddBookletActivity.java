package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.StudyAddResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookletActivity extends AppCompatActivity {
    private EditText ed_title, ed_lesson;
    private TextView upload_file;
    private String TAG = "AddBookletActivity";
    private boolean permissiongranted;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private File mainfile = new File("null");
    private Button btnSubTutorial;
    private File main = new File("null");
    private String _staffid = "";
    private ProgressDialog progressDialog;
    private Intent mainintent;
    private String chapterid = "", subject_id = "", chapter_name = "", action = "";
    private String title = "", description = "", running_id = "";
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booklet);
        ed_title = findViewById(R.id.ed_book_title);
        ed_lesson = findViewById(R.id.ed_book_description);
        upload_file = findViewById(R.id.upload_btn);
        btnSubTutorial = findViewById(R.id.btnSubTutorial);
        mainintent = getIntent();
        backbtn = findViewById(R.id.backbtn);
        title = mainintent.getStringExtra("title");
        description = mainintent.getStringExtra("lesson");
        running_id = mainintent.getStringExtra("running_id");
        chapterid = mainintent.getStringExtra("chapterid");
        subject_id = mainintent.getStringExtra("subject_id");
        chapter_name = mainintent.getStringExtra("chapter_name");
        action = mainintent.getStringExtra("action");
        Log.d(TAG, "chapterid " + chapterid + "subjectid" + subject_id);
        if (action.equals("edit")) {
            ed_title.setText(title);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ed_lesson.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                ed_lesson.setText(Html.fromHtml(description));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name_main));
        progressDialog.setMessage("Loading....");
        progressDialog.create();
        final Staff staff = SharedPrefManager2.getInstance(AddBookletActivity.this).getStaff();
        _staffid = staff.getId();
        Log.d(TAG, _staffid + " staffid");
        checkAndRequestPermissions();
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(AddBookletActivity.this, "", 0);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_title.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!ed_lesson.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!mainfile.getPath().equals("null")) {
                            if (InternetCheck.isInternetOn(AddBookletActivity.this) == true) {
                                if (action.equals("add")) {
                                    uploadBooklet();
                                } else if (action.equals("edit")) {
                                    uploadEditedBooklet();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select Image", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            showsnackbar();
                        }
                    } else {
                        ed_lesson.setError("Please enter Book Description");
                    }

                } else {
                    ed_title.setError("Please enter Book Title");
                }
            }
        });
    }

    private void uploadEditedBooklet() {
        progressDialog.show();
        try {
            main = new Compressor(this).compressToFile(mainfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "1" + subject_id + "2" + ed_title.getText().toString() + "3" + main + "4" + chapterid + "5 " + ed_lesson.getText().toString() + "6 " + _staffid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"), subject_id);
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"), ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"), "active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);
        RequestBody a_running_id = RequestBody.create(MediaType.parse("text/plain"), running_id);

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().edit_lms_image(a_subject_id, a_book_name, requestFile, a_chapter_id, a_book_desc, a_status, a_user_type, a_user_id, a_running_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG, " response: " + response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddBookletActivity.this, StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid", chapterid);
                    intent.putExtra("subject_id", subject_id);
                    intent.putExtra("chaptername", chapter_name);
                    intent.putExtra("type","image");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Images Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudyAddResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadBooklet() {
        progressDialog.show();
        try {
            main = new Compressor(this).compressToFile(mainfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "1" + subject_id + "2" + ed_title.getText().toString() + "3" + main + "4" + chapterid + "5 " + ed_lesson.getText().toString() + "6 " + _staffid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"), subject_id);
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"), ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"), "active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().add_lms_image(a_subject_id, a_book_name, requestFile, a_chapter_id, a_book_desc, a_status, a_user_type, a_user_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG, " response: " + response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddBookletActivity.this, StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid", chapterid);
                    intent.putExtra("subject_id", subject_id);
                    intent.putExtra("chaptername", chapter_name);
                    intent.putExtra("type","image");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Images Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudyAddResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
        EasyImage.handleActivityResult(requestCode, resultCode, data, AddBookletActivity.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                upload_file.setText("Image Selected");
                mainfile = imageFiles.get(0);
            }
        });
    }

    private void showsnackbar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
