package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.school.iqdigit.Activity.EnquiryActivity.EMAIL_ADDRESS_PATTERN;

public class JobApplyActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backbtn;
    private Button btnSubmit1;
    private EditText edEmail, edNameCandidate, edPost, edMobileNumber;
    private TextView upload_resume;
    private int STORAGE_PERMISSION = 1;
    private int CAMERA_PERMISSION = 11;
    File mainfile = new File("null");
    private String TAG = "JobApplyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_apply);
        backbtn = findViewById(R.id.backbtn);
        btnSubmit1 = findViewById(R.id.btnSubmit1);
        edEmail = findViewById(R.id.edEmail);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        edNameCandidate = findViewById(R.id.edNameCandidate);
        edPost = findViewById(R.id.edPost);
        upload_resume = findViewById(R.id.upload_resume);
        btnSubmit1.setOnClickListener(this);
        upload_resume.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(JobApplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assignment")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(JobApplyActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                }, STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                }, STORAGE_PERMISSION);
            }
        }
        if (ContextCompat.checkSelfPermission(JobApplyActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assignment")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(JobApplyActivity.this, new String[]{Manifest.permission.CAMERA
                                }, CAMERA_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                }, CAMERA_PERMISSION);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit1: {
                if (!edNameCandidate.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!edPost.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!edMobileNumber.getText().toString().trim().equalsIgnoreCase("")) {
                            if (edMobileNumber.getText().toString().trim().length() >= 10) {
                                if (!edEmail.getText().toString().trim().equalsIgnoreCase("")) {
                                    if (EMAIL_ADDRESS_PATTERN.matcher(edEmail.getText().toString()).matches()) {
                                        try {
                                            if (!mainfile.getPath().equals("null"))
                                                uploadResume1();
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select Resume Image", Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        edEmail.setError("Please enter valid email");
                                    }
                                } else {
                                    edEmail.setError("Please enter Your Email");
                                }
                            } else {
                                edMobileNumber.setError("Please valid Mobile number");
                            }
                        } else {
                            edMobileNumber.setError("Please enter Your Mobile number");
                        }
                    } else {
                        edPost.setError("Please enter Post you want to apply");
                    }

                } else {
                    edNameCandidate.setError("Please enter Your Name");
                }
                break;
            }
            case R.id.upload_resume: {
                EasyImage.openChooserWithGallery(JobApplyActivity.this, "", 0);
                break;
            }
            case R.id.backbtn: {
                onBackPressed();
                break;
            }
        }
    }

    private void uploadResume1() throws IOException {
        File main = new Compressor(this).compressToFile(mainfile);
        String mainfilestring = mainfile.toString();
        Log.v(TAG, mainfile.toString());
        //creating request body for
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
        RequestBody a_name = RequestBody.create(MediaType.parse("text/plain"), edNameCandidate.getText().toString());
        RequestBody a_post = RequestBody.create(MediaType.parse("text/plain"), edPost.getText().toString());
        RequestBody a_mobile = RequestBody.create(MediaType.parse("text/plain"), edMobileNumber.getText().toString());
        RequestBody a_email = RequestBody.create(MediaType.parse("text/plain"), edEmail.getText().toString());


        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadResume(requestFile, a_name, a_post, a_mobile, a_email);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse defaultResponse = response.body();
                // Log.d(TAG , defaultResponse.isErr()+" ");
                if (defaultResponse.isErr() == false) {
                    edNameCandidate.setText("");
                    edPost.setText("");
                    edMobileNumber.setText("");
                    edEmail.setText("");
                    upload_resume.setText("Select Image");
                    Toast.makeText(getApplicationContext(), "Resume Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, JobApplyActivity.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                upload_resume.setText("Image Selected");
                mainfile = imageFiles.get(0);
            }
        });

    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

}
