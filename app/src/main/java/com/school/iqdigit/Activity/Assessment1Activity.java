package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.school.iqdigit.Adapter.AssessmentImagesAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.GetClikedImagePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class Assessment1Activity extends AppCompatActivity implements View.OnClickListener, GetClikedImagePath {
    private RecyclerView recyclerView_pic;
    private LinearLayoutManager layoutManager;
    private ImageView img_pic,backbtn;
    private FloatingActionButton img_click_pic;
    private static final int CAMERA_REQUEST = 1888;
    private EditText edRemarks;
    private int PICK_IMAGE_REQUEST = 100;
    private Uri file;
    private Uri imageUri;
    private String TAG = "Assessment1Activity";
    private ArrayList<String> assessment_images;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private AssessmentImagesAdapter assessmentImagesAdapter;
    private boolean permissiongranted;
    private Button submit;
    private Bitmap bitmap;
    private String hw_id = "";
    private String pathname;
    private ProgressDialog progressDialog;
    private TextView text_no_available;
    Intent mainintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment1);
        mainintent = getIntent();
        hw_id = mainintent.getStringExtra("hw_id");
        edRemarks = findViewById(R.id.edRemarks);
        checkAndRequestPermissions();
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        recyclerView_pic = findViewById(R.id.recycleView_pic);
        img_pic = findViewById(R.id.img_pic);
        img_pic.setOnClickListener(this);
        img_click_pic = findViewById(R.id.img_click_pic);
        img_click_pic.setOnClickListener(this);
        submit = findViewById(R.id.btn_submit);
        text_no_available = findViewById(R.id.text_no_available);
        submit.setOnClickListener(this);
        assessment_images = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name_main));
        progressDialog.setMessage("Loading....");
        progressDialog.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_click_pic:
                EasyImage.openChooserWithGallery(Assessment1Activity.this, "", 0);
//                selectImageDialog();
                break;
            case R.id.btn_submit:
                if (assessment_images.size() > 0) {
                    createPDFNew();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Add image", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.backbtn:
                startActivity(new Intent(Assessment1Activity.this,AssesmentStudentActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void savePath(String path) {
        Glide.with(this)
                .load(Uri.parse(path))
                .into(img_pic);
    }

    @Override
    public void deleteImage(int deletedImagePosition) {
        assessment_images.remove(deletedImagePosition);
        assessmentImagesAdapter.notifyDataSetChanged();
        if(deletedImagePosition == 0)
            text_no_available.setVisibility(View.VISIBLE);
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
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "camera services permission granted");
                    permissiongranted = true;
                } else {
                    Log.d(TAG, "camera are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ) {
                        showDialogOK("Storage Permissions are required for this app",
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
        EasyImage.handleActivityResult(requestCode, resultCode, data, Assessment1Activity.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File compressed = null;
                try {
                    compressed = new Compressor(Assessment1Activity.this)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(imageFiles.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri =  Uri.fromFile(compressed);
                Log.d(TAG, "file_path" + imageUri);
                assessment_images.add(String.valueOf(imageUri));
                Log.d(TAG, "images: " + assessment_images.size());
                assessmentImagesAdapter = new AssessmentImagesAdapter(Assessment1Activity.this, assessment_images, Assessment1Activity.this);
                layoutManager = new LinearLayoutManager(Assessment1Activity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView_pic.setLayoutManager(layoutManager);
                recyclerView_pic.setAdapter(assessmentImagesAdapter);
                Glide.with(Assessment1Activity.this)
                        .load(imageUri)
                        .into(img_pic);
                text_no_available.setVisibility(View.GONE);
            }
        });
    }
    private void createPDFNew() {
        progressDialog.show();
        File  dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "assessment.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dir.getAbsolutePath() + "/assessment.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        for (int i = 0; i < assessment_images.size(); i++) {
            Image image = null;
            try {
                image = Image.getInstance(assessment_images.get(i));
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100;
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            try {
                document.add(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        document.close();
        if (!edRemarks.getText().toString().equals("")) {
            uploadpdf(file);
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Type Remarks", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadpdf(File pdfPath) {
        Log.d(TAG , pdfPath+" filename");
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), pdfPath);
       // RequestBody id = RequestBody.create(MediaType.parse("text/plain"), "104");
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), hw_id);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), edRemarks.getText().toString());

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadPdf(requestFile, id, remarks);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse defaultResponse = response.body();
                // Log.d(TAG , defaultResponse.isErr()+" ");
                if (defaultResponse.isErr() == false) {
                    assessment_images.clear();
                    assessmentImagesAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Assessment Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG ,  t.getMessage());
            }
        });
    }
}
