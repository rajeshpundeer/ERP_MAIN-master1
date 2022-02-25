package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.school.iqdigit.Adapter.AssessmentImagesAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.interfaces.GetClikedImagePath;
import com.school.iqdigit.utility.InternetCheck;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.aprilapps.easyphotopicker.Constants.DEFAULT_FOLDER_NAME;

public class AssignmentStaff extends AppCompatActivity implements GetClikedImagePath {
    private Spinner spinner, spinner2;
    private NiceSpinner spRemarks;
    private Gson gson;
    private EditText title, description, edMaximum;
    private LinearLayout llAssessment, llMaximum;
    private TextView upload_file;
    private Button choose_date;
    private ImageView submit, backbtn;
    private Uri selectedImage;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ProgressDialog progressDialog;
    private int STORAGE_PERMISSION = 1;
    private CheckBox chkAssessment;
    private String assessment = "0";
    File mainfile = new File("null");
    int year;
    int month;
    int day;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;
    String mCurrentPhotoPath;
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 100;
    private String pictureFilePath;
    //Submit data elements
    private String _title = "", _description = "", _ldate = "", _cdate = "", _classid = "", _subjectid = "", _staffid = "";
    private ArrayList<String> remarks = new ArrayList<>();
    private ArrayList<String> pictype = new ArrayList<>();
    private String remarksitem = "Rating";
    private String imageitem = "Insert Single Image";
    private String max = "";
    private RelativeLayout rl_Images;
    private ImageView img_click_pic;
    private TextView text_no_available, upload_pdf_btn;
    private ArrayList<String> assessment_images;
    private Uri imageUri;
    private RecyclerView recyclerView_pic;
    private LinearLayoutManager layoutManager;
    private AssessmentImagesAdapter assessmentImagesAdapter;
    private File main;
    private CheckBox chkPages;
    private File mainpdf = new File("null");
    private RequestBody requestPdf;
    private RequestBody requestFile;
    private Uri filePath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_staff);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Assignment");
        progressDialog.setMessage("Wait a moment while \nassignment is Uploading....");
        progressDialog.create();
        progressDialog.setCancelable(false);
        title = findViewById(R.id.title_sa);
        description = findViewById(R.id.description_sa);
        upload_file = findViewById(R.id.upload_btn);
        choose_date = findViewById(R.id.date_btn);
        chkAssessment = findViewById(R.id.chkAssessment);
        spRemarks = findViewById(R.id.spRemarks);
        edMaximum = findViewById(R.id.edMaximum);
        llAssessment = findViewById(R.id.llAssessment);
        llMaximum = findViewById(R.id.llMaximum);
        //choose_date.setVisibility(View.GONE);
        submit = findViewById(R.id.next);
        //  submit.setVisibility(View.GONE);
        backbtn = findViewById(R.id.backbtn);
        spinner = findViewById(R.id.spCompany);
        spinner2 = findViewById(R.id.spSubject);
        chkPages = findViewById(R.id.chkPages);
        rl_Images = findViewById(R.id.rl_Images);
        img_click_pic = findViewById(R.id.img_click_pic);
        text_no_available = findViewById(R.id.text_no_available);
        assessment_images = new ArrayList<>();
        recyclerView_pic = findViewById(R.id.recycleView_pic);
        upload_pdf_btn = findViewById(R.id.upload_pdf_btn);

        getcurrentattent();
        final Staff staff = SharedPrefManager2.getInstance(AssignmentStaff.this).getStaff();
        _staffid = staff.getId();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if ((ContextCompat.checkSelfPermission(AssignmentStaff.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(AssignmentStaff.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(AssignmentStaff.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assignment")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA},
                                            STORAGE_PERMISSION);
                                }
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
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        STORAGE_PERMISSION);
            }
        }
        chkAssessment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkAssessment.isChecked()) {
                    llAssessment.setVisibility(View.GONE);
                } else {
                    llAssessment.setVisibility(View.GONE);
                }
            }
        });

        chkPages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkPages.isChecked()) {
                    upload_file.setVisibility(View.GONE);
                    // upload_pdf_btn.setVisibility(View.GONE);
                    rl_Images.setVisibility(View.VISIBLE);
                } else {
                    upload_file.setVisibility(View.VISIBLE);
                    //   upload_pdf_btn.setVisibility(View.VISIBLE);
                    rl_Images.setVisibility(View.GONE);
                }
            }
        });

        //add items to arraylist
        remarks.add("Rating");
        remarks.add("Grade");
        remarks.add("Percentage");
        remarks.add("Marks");
        remarks.add("N/A");

        //add items to remarks spinner
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(AssignmentStaff.this, android.R.layout.simple_list_item_1, remarks);
        //setting adapter to spinner
        spRemarks.setAdapter(adapter);
        spRemarks.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                remarksitem = (String) parent.getItemAtPosition(position);
                if (remarksitem.equals("Marks")) {
                    llMaximum.setVisibility(View.GONE);
                    max = edMaximum.getText().toString();
                } else {
                    llMaximum.setVisibility(View.GONE);
                }
            }
        });

        // add items to picture spinner
        pictype.add("Insert Single Image");
        pictype.add("Insert Multiple Images");
        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<String>(AssignmentStaff.this, android.R.layout.simple_list_item_1, pictype);
        //setting adapter to spinner
        img_click_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(AssignmentStaff.this, "", 0);
            }
        });

        if (InternetCheck.isInternetOn(AssignmentStaff.this) == true) {
            getClasses();
        } else {
            showsnackbar();
        }
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _title = title.getText().toString();
                _description = description.getText().toString();
                if (!_title.equals("")) {
                    if (!_description.equals("")) {
                        if (!_classid.equals("")) {
                            if (!_subjectid.equals("")) {
                                EasyImage.openChooserWithGallery(AssignmentStaff.this, "", 0);
                                //selectImage();
                            } else {
                                Toast.makeText(AssignmentStaff.this, "Please select subject for assignment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AssignmentStaff.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        description.setError("Please enter description for assignment");
                    }
                } else {
                    title.setError("Please enter title for assignment");
                }
            }
        });

        upload_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _title = title.getText().toString();
                _description = description.getText().toString();
                if (!_title.equals("")) {
                    if (!_description.equals("")) {
                        if (!_classid.equals("")) {
                            if (!_subjectid.equals("")) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 1);
                            } else {
                                Toast.makeText(AssignmentStaff.this, "Please select subject for assignment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AssignmentStaff.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        description.setError("Please enter description for assignment");
                    }
                } else {
                    title.setError("Please enter title for assignment");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetCheck.isInternetOn(AssignmentStaff.this) == true) {
                    _staffid = staff.getId();
                    _title = title.getText().toString();
                    _description = description.getText().toString();
                    if (!_title.equals("")) {
                        if (!_description.equals("")) {
                            if (!_ldate.equals("") && !_cdate.equals("")) {
                                if (!_classid.equals("")) {
                                    if (!_subjectid.equals("")) {
                                        if (llMaximum.getVisibility() == View.VISIBLE) {
                                            if (!edMaximum.getText().toString().equals("")) {
                                                progressDialog.show();
                                                if (!mainfile.getPath().equals("null")) {
                                                    try {
                                                        uploadFile(selectedImage);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    progressDialog.show();
                                                    try {
                                                        bytearrayoutputstream = new ByteArrayOutputStream();
                                                        Drawable drawable = getResources().getDrawable(R.drawable.homework_pic);
                                                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
                                                        file = new File(Environment.getExternalStorageDirectory() + "/SampleImage.png");
                                                        try {
                                                            file.createNewFile();
                                                            FileOutputStream fileoutputstream = new FileOutputStream(file);
                                                            fileoutputstream.write(bytearrayoutputstream.toByteArray());
                                                            fileoutputstream.close();
                                                            Log.d(TAG, file.getAbsolutePath());
                                                            mainfile = new File(file.getAbsolutePath());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (!mainpdf.getPath().equals("null") || assessment_images.size() > 0) {
                                                            uploadFile(selectedImage);
                                                        } else {
                                                            Toast.makeText(AssignmentStaff.this, "Please select Pdf or Image", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(AssignmentStaff.this, "Please enter maximum marks for Assessment", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            progressDialog.show();
                                            try {
                                                uploadFile(selectedImage);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                           /* if (!mainfile.getPath().equals("null")) {

                                            } else {
                                                progressDialog.show();
                                                try {
                                                    bytearrayoutputstream = new ByteArrayOutputStream();
                                                    Drawable drawable = getResources().getDrawable(R.drawable.homework_pic);
                                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
                                                    file = new File(Environment.getExternalStorageDirectory() + "/SampleImage.png");
                                                    try {
                                                        file.createNewFile();
                                                        FileOutputStream fileoutputstream = new FileOutputStream(file);
                                                        fileoutputstream.write(bytearrayoutputstream.toByteArray());
                                                        fileoutputstream.close();
                                                        Log.d(TAG, file.getAbsolutePath());
                                                        mainfile = new File(file.getAbsolutePath());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (!mainpdf.getPath().equals("null") || assessment_images.size() > 0) {
                                                        uploadFile(selectedImage);
                                                    } else {
                                                        Toast.makeText(AssignmentStaff.this, "Please select Pdf or Image", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }*/
                                        }

                                    } else {
                                        Toast.makeText(AssignmentStaff.this, "Please select subject for assignment", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AssignmentStaff.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AssignmentStaff.this, "Please choose date for assignment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            description.setError("Please enter description for assignment");
                        }
                    } else {
                        title.setError("Please enter title for assignment");
                    }
                } else {
                    showsnackbar();
                }
            }
        });

        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AssignmentStaff.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);  //2018+"-"
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                _ldate = yy + "-" + mm + "-" + dd;
                                _cdate = yy + "-" + mm + "-" + dd;
                                choose_date.setText(_ldate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void getcurrentattent() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String yy = String.valueOf(year);
        String dd = String.valueOf(day);
        month = month + 1;
        String mm = String.valueOf(month);
        if (month < 10) {
            mm = "0" + mm;
        }
        if (day < 10) {
            dd = "0" + dd;
        }
        String getdate = yy + "-" + mm + "-" + dd;
        choose_date.setText(getdate);
        _ldate = getdate;
        _cdate = getdate;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, AssignmentStaff.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                if (upload_file.getVisibility() == View.VISIBLE) {
                    submit.setVisibility(View.VISIBLE);
                    upload_file.setText("Image Selected");
                    choose_date.setVisibility(View.VISIBLE);
                    mainfile = imageFiles.get(0);
                } else if (rl_Images.getVisibility() == View.VISIBLE) {
                    File compressed = null;
                    try {
                        compressed = new Compressor(AssignmentStaff.this)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(compressed);
                    Log.d(TAG, "file_path" + imageUri);
                    assessment_images.add(String.valueOf(imageUri));
                    Log.d(TAG, "vehical images: " + assessment_images.size());
                    assessmentImagesAdapter = new AssessmentImagesAdapter(AssignmentStaff.this, assessment_images, AssignmentStaff.this);
                    layoutManager = new LinearLayoutManager(AssignmentStaff.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView_pic.setLayoutManager(layoutManager);
                    recyclerView_pic.setAdapter(assessmentImagesAdapter);
                    text_no_available.setVisibility(View.GONE);
                }
            }
        });
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            upload_file.setText("Image Selected");
            choose_date.setVisibility(View.VISIBLE);
            mainfile = new File(pictureFilePath);
            addToGallery();
        } else if (requestCode == 200) {
            selectedImage = data.getData();
            upload_file.setText("Image Selected");
            choose_date.setVisibility(View.VISIBLE);
            mainfile = new File(getRealPathFromURI(selectedImage));
        } else if (resultCode == RESULT_OK && requestCode == 1) {
            filePath = data.getData();
            try {
                mainpdf = new File("null");
                mainpdf = pickedExistingPicture(this, filePath);
                if (upload_pdf_btn.getVisibility() == View.VISIBLE) {
                    upload_pdf_btn.setText("pdf Selected");
                    Log.d(TAG, filePath + "filepath");
                    Log.d(TAG, mainpdf + " mainpdfFile");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        galleryIntent.setData(picUri);
        this.sendBroadcast(galleryIntent);
    }

    static File pickedExistingPicture(@NonNull Context context, Uri photoUri) throws IOException {
        InputStream pictureInputStream = context.getContentResolver().openInputStream(photoUri);
        File directory = tempImageDirectory(context);
        File photoFile = new File(directory, UUID.randomUUID().toString() + "." + getMimeType(context, photoUri));
        photoFile.createNewFile();
        writeToFile(pictureInputStream, photoFile);
        return photoFile;
    }

    private static File tempImageDirectory(@NonNull Context context) {
        File privateTempDir = new File(context.getCacheDir(), DEFAULT_FOLDER_NAME);
        if (!privateTempDir.exists()) privateTempDir.mkdirs();
        return privateTempDir;
    }

    private static String getMimeType(@NonNull Context context, @NonNull Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    private static void writeToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getSubjects(String classid) {
        final String[][] totalSubjectsid = new String[1][1];
        Call<SubjectResponse> call = RetrofitClient.getInstance().getApi().getSubjects(classid);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                List<Subjects> subjects;
                SubjectResponse subjectResponse = response.body();
                if (response.body().getSubjects().size() > 0) {
                    subjects = subjectResponse.getSubjects();
                    //Creating an String array for the ListView
                    String[] totalSubjects = new String[subjects.size()];
                    totalSubjectsid[0] = new String[subjects.size()];

                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < subjects.size(); i++) {
                        totalSubjects[i] = subjects.get(i).getSubject_name();
                        totalSubjectsid[0][i] = subjects.get(i).getSubject_id();
                    }

                    ArrayAdapter<String> adapter2;
                    adapter2 = new ArrayAdapter<String>(AssignmentStaff.this, android.R.layout.simple_list_item_1, totalSubjects);
                    //setting adapter to spinner
                    spinner2.setAdapter(adapter2);
                    _subjectid = totalSubjectsid[0][0];
                } else {
                    ShowNotificationPop("Please Add Subjects for selected class in ERP");
                }

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String subbjectid = totalSubjectsid[0][position];
                _subjectid = subbjectid;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getClasses() {
        final String[][] totalclassesid = new String[1][1];
        Call<ClassResponse> call = RetrofitClient.getInstance().getApi().getClasses(_staffid);
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                List<Classes> classes;

                ClassResponse classResponse = response.body();

                classes = classResponse.getClasses();
                Log.d(TAG, classes.size() + " classlist");
                //Creating an String array for the ListView
                String[] totalclasses = new String[classes.size()];
                totalclassesid[0] = new String[classes.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < classes.size(); i++) {
                    totalclasses[i] = classes.get(i).getClass_name();
                    totalclassesid[0][i] = classes.get(i).getClass_id();
                }

                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>(AssignmentStaff.this, android.R.layout.simple_list_item_1, totalclasses);
                //setting adapter to spinner
                spinner.setAdapter(adapter);

                _classid = totalclassesid[0][0];
                getSubjects(_classid);
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String classid = totalclassesid[0][position];
                _classid = classid;
                if (InternetCheck.isInternetOn(AssignmentStaff.this) == true) {
                    getSubjects(_classid);
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AssignmentStaff.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    sendTakePictureIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 200);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void sendTakePictureIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        pictureFile);
                selectedImage = photoURI;
                Toast.makeText(this, photoURI.toString().trim(), Toast.LENGTH_SHORT).show();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "IQDIGIT_" + timeStamp;
        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    private void uploadFile(Uri fileUri) throws IOException {
        assessment = "0";
        if (rl_Images.getVisibility() == View.VISIBLE) {
            if (assessment_images.size() > 0) {
                createPDFNew();
                Log.d(TAG, mainpdf + " pdf");
            }
        }
        if (!mainfile.getPath().equals("null")) {
            main = new Compressor(this).compressToFile(mainfile);
        }
        max = edMaximum.getText().toString();
        if (remarksitem.equalsIgnoreCase("Marks")) {
            if (!max.equals("")) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                Log.d(TAG, main + " image");
                if (rl_Images.getVisibility() == View.VISIBLE) {
                    if (assessment_images.size() > 0) {
                        requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                        Log.d(TAG, mainpdf + " pdf");
                    } else {
                        requestPdf = RequestBody.create(MediaType.parse("application/pdf"), "");
                    }
                }
                if (!mainpdf.getPath().equals("null")) {
                    requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                    Log.d(TAG, mainpdf + "direct pdf");
                }

                RequestBody a_title = RequestBody.create(MediaType.parse("text/plain"), _title);
                RequestBody a_description = RequestBody.create(MediaType.parse("text/plain"), _description);
                RequestBody a_classid = RequestBody.create(MediaType.parse("text/plain"), _classid);
                RequestBody a_subid = RequestBody.create(MediaType.parse("text/plain"), _subjectid);
                RequestBody a_ldate = RequestBody.create(MediaType.parse("text/plain"), _ldate);
                RequestBody a_cdate = RequestBody.create(MediaType.parse("text/plain"), _cdate);
                RequestBody a_staffid = RequestBody.create(MediaType.parse("text/plain"), _staffid);
                RequestBody assessment_check = RequestBody.create(MediaType.parse("text/plain"), assessment);
                RequestBody unit = RequestBody.create(MediaType.parse("text/plain"), remarksitem);
                RequestBody max1 = RequestBody.create(MediaType.parse("text/plain"), max);
                RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), "");
                RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), "");
                RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), "");
                RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), "");
                RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), "");

                Log.d(TAG, assessment + " assessment");
                RequestBody negative_marks_count = RequestBody.create(MediaType.parse("text/plain"), "");

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count, negative_marks_count);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse defaultResponse = response.body();
                        if (!defaultResponse.isErr()) {
                            title.setText("");
                            description.setText("");
                            edMaximum.setText("");
                            chkAssessment.setChecked(false);
                            llAssessment.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            startActivity(new Intent(AssignmentStaff.this, AssignmentStaffView.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(AssignmentStaff.this, "Please Enter Maximum marks", Toast.LENGTH_LONG).show();
            }
        } else {
            if(!mainfile.getPath().equals("null")) {
                requestFile = RequestBody.create(MediaType.parse("image/*"), main);
            }else {
                requestFile = RequestBody.create(MediaType.parse("image/*"), "");
            }
            if (rl_Images.getVisibility() == View.VISIBLE) {
                if (assessment_images.size() > 0) {
                    requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                    Log.d(TAG, mainpdf + " pdf");
                } else {
                    assessment_images.clear();
                    requestPdf = RequestBody.create(MediaType.parse("application/pdf"), "");
                }
            }


            RequestBody a_title = RequestBody.create(MediaType.parse("text/plain"), _title);
            RequestBody a_description = RequestBody.create(MediaType.parse("text/plain"), _description);
            RequestBody a_classid = RequestBody.create(MediaType.parse("text/plain"), _classid);
            RequestBody a_subid = RequestBody.create(MediaType.parse("text/plain"), _subjectid);
            RequestBody a_ldate = RequestBody.create(MediaType.parse("text/plain"), _ldate);
            RequestBody a_cdate = RequestBody.create(MediaType.parse("text/plain"), _cdate);
            RequestBody a_staffid = RequestBody.create(MediaType.parse("text/plain"), _staffid);
            RequestBody assessment_check = RequestBody.create(MediaType.parse("text/plain"), assessment);
            RequestBody unit = RequestBody.create(MediaType.parse("text/plain"), remarksitem);
            RequestBody max1 = RequestBody.create(MediaType.parse("text/plain"), max);
            RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), "");

            Log.d(TAG, assessment + " assessment " + requestPdf);
            RequestBody negative_marks_count = RequestBody.create(MediaType.parse("text/plain"), "");

            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count, negative_marks_count);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    DefaultResponse defaultResponse = response.body();
                    if (!defaultResponse.isErr()) {
                        title.setText("");
                        description.setText("");
                        edMaximum.setText("");
                        chkAssessment.setChecked(false);
                        llAssessment.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        startActivity(new Intent(AssignmentStaff.this, AssignmentStaffView.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, t.getMessage());
                    //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity_img to handle the intent
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, 100);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Toast.makeText(this, "" + mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
        return image;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
                        if (InternetCheck.isInternetOn(AssignmentStaff.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void savePath(String path) {
    }

    @Override
    public void deleteImage(int deletedImagePosition) {
        assessment_images.remove(deletedImagePosition);
        assessmentImagesAdapter.notifyDataSetChanged();
        if (deletedImagePosition == 0)
            text_no_available.setVisibility(View.VISIBLE);
    }

    private void createPDFNew() {
        progressDialog.show();
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        if (!dir.exists())
            dir.mkdirs();
        mainpdf = new File(dir, "assessment.pdf");
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
    }

    public void ShowNotificationPop(String txt) {
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk;
        dialog = new Dialog(AssignmentStaff.this);
        dialog.setContentView(R.layout.ic_common_text_popup);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textView.setText(txt);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onBackPressed();
            }
        };
    }
}