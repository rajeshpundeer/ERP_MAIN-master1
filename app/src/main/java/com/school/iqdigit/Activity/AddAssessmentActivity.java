package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.text.ParseException;
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


public class AddAssessmentActivity extends AppCompatActivity implements GetClikedImagePath {
    private NiceSpinner  spRemarks;
    private Spinner spinner, spinner2;
    private Gson gson;
    private EditText title, description, edMaximum;
    private LinearLayout llMaximum;
    private TextView upload_file;
    private Button choose_date, btnEnd;
    private ImageView backbtn;
    private Button submit;
    private LinearLayout lltimebound, llMcq, llAssessment;
    private Uri selectedImage;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ProgressDialog progressDialog;
    private int STORAGE_PERMISSION = 1;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean permissiongranted;
    private String assessment = "1";
    RequestBody requestFile;
    File mainfile = new File("null");
    int year, year1;
    int month, month1;
    int day, day1;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    private static final String TAG = "AddAssessmentActivity";
    static final int REQUEST_PICTURE_CAPTURE = 100;
    private String pictureFilePath;
    private String _title = "", _description = "", _ldate = "", _cdate = "", _classid = "", _subjectid = "", _staffid = "", _cdate1 = "";
    private ArrayList<String> remarks = new ArrayList<>();
    private String remarksitem = "Rating";
    String max = "";
    private int mHour = 0;
    private int mMinute = 0;
    private int mHour1 = 0;
    private int mMinute1 = 0;
    private String startdate = "", enddate = "", edate = "";
    private String _cdate2 = "", sdate = "";
    private CheckBox chkAssessment;
    private String time_bound1 = "0";
    long different, difference1;
    private LinearLayout rl_Images;
    private ImageView img_click_pic;
    private TextView text_no_available;
    private ArrayList<String> assessment_images;
    private Uri imageUri;
    private RecyclerView recyclerView_pic;
    private LinearLayoutManager layoutManager;
    private AssessmentImagesAdapter assessmentImagesAdapter;
    private ArrayList<String> pictype = new ArrayList<>();
    private CheckBox chkPages;
    private File mainpdf = new File("null");
    private RequestBody requestPdf;
    private RadioButton radio_mcq, radio_subjective;
    private EditText ed_mcqno, ed_mcq_marks, ed_nagitive_marking;;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Assessment");
        progressDialog.setMessage("Wait a moment while \nassessment is Uploading....");
        progressDialog.create();
        title = findViewById(R.id.title_sa);
        description = findViewById(R.id.description_sa);
        upload_file = findViewById(R.id.upload_btn);
        choose_date = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        spRemarks = findViewById(R.id.spRemarks);
        edMaximum = findViewById(R.id.edMaximum);
        llMaximum = findViewById(R.id.llMaximum);
        chkAssessment = findViewById(R.id.chkAssessment);
        submit = findViewById(R.id.next);
        backbtn = findViewById(R.id.backbtn);
        spinner = findViewById(R.id.spCompany);
        spinner2 = findViewById(R.id.spSubject);
        lltimebound = findViewById(R.id.lltimebound);
        chkPages = findViewById(R.id.chkPages);
        rl_Images = findViewById(R.id.rl_Images);
        img_click_pic = findViewById(R.id.img_click_pic);
        text_no_available = findViewById(R.id.text_no_available);
        assessment_images = new ArrayList<>();
        recyclerView_pic = findViewById(R.id.recycleView_pic);
        ed_nagitive_marking = findViewById(R.id.ed_nagitive_marking);
        radio_mcq = findViewById(R.id.radio_mcq);
        radio_subjective = findViewById(R.id.radio_subjective);
        llMcq = findViewById(R.id.llMcq);
        llAssessment = findViewById(R.id.llAssessment);
        ed_mcq_marks = findViewById(R.id.ed_mcq_marks);
        ed_mcqno = findViewById(R.id.ed_mcqno);


        radio_subjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_subjective.isChecked()) {
                    assessment = "1";
                    llAssessment.setVisibility(View.VISIBLE);
                    llMcq.setVisibility(View.GONE);

                }
            }
        });
        radio_mcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_mcq.isChecked()) {
                    assessment = "2";
                    llAssessment.setVisibility(View.GONE);
                    llMcq.setVisibility(View.VISIBLE);
                }
            }
        });
        // to get current date
        getcurrentattent();

        final Staff staff = SharedPrefManager2.getInstance(AddAssessmentActivity.this).getStaff();
        _staffid = staff.getId();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAssessmentActivity.this, AssessmentstaffActivity.class));
                finish();
            }
        });

        if ((ContextCompat.checkSelfPermission(AddAssessmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(AddAssessmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(AddAssessmentActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assessment")
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

        //add items to arraylist
        remarks.add("Rating");
        remarks.add("Grade");
        remarks.add("Percentage");
        remarks.add("Marks");
        remarks.add("N/A");

        //add items to remarks spinner
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(AddAssessmentActivity.this, android.R.layout.simple_list_item_1, remarks);
        //setting adapter to spinner
        spRemarks.setAdapter(adapter);
        spRemarks.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                remarksitem = (String) parent.getItemAtPosition(position);
                if (remarksitem.equals("Marks")) {
                    llMaximum.setVisibility(View.VISIBLE);
                    max = edMaximum.getText().toString();
                } else {
                    llMaximum.setVisibility(View.GONE);
                }
            }
        });

        chkPages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkPages.isChecked()) {
                    upload_file.setVisibility(View.GONE);
                    rl_Images.setVisibility(View.VISIBLE);
                } else {
                    upload_file.setVisibility(View.VISIBLE);
                    rl_Images.setVisibility(View.GONE);
                }
            }
        });

        img_click_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(AddAssessmentActivity.this, "", 0);
            }
        });

        chkAssessment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkAssessment.isChecked()) {
                    lltimebound.setVisibility(View.VISIBLE);
                } else {
                    lltimebound.setVisibility(View.GONE);
                }
            }
        });
        if (InternetCheck.isInternetOn(AddAssessmentActivity.this) == true) {
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
                                EasyImage.openChooserWithGallery(AddAssessmentActivity.this, "", 0);
                                //selectImage();
                            } else {
                                Toast.makeText(AddAssessmentActivity.this, "Please select subject for assignment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddAssessmentActivity.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
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
                if (InternetCheck.isInternetOn(AddAssessmentActivity.this) == true) {
                    _staffid = staff.getId();
                    _title = title.getText().toString();
                    _description = description.getText().toString();
                    if (!_title.equals("")) {
                        if (!_description.equals("")) {
                            if (!_classid.equals("")) {
                                if (!_subjectid.equals("")) {
                                    if (llMaximum.getVisibility() == View.VISIBLE) {
                                        if (!edMaximum.getText().toString().equals("")) {
                                            progressDialog.show();
                                            if (!mainfile.getPath().equals("null") || assessment_images.size() > 0 ) {
                                                try {
                                                    uploadFile(selectedImage);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddAssessmentActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(AddAssessmentActivity.this, "Please enter maximum marks for Assessment", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (llMcq.getVisibility() == View.VISIBLE) {
                                            if (!ed_mcqno.getText().toString().trim().equals("")) {
                                                if (!ed_mcq_marks.getText().toString().trim().equals("")) {
                                                    progressDialog.show();
                                                    if (!mainfile.getPath().equals("null") || assessment_images.size() > 0 ) {
                                                        try {
                                                            uploadFile(selectedImage);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddAssessmentActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    ed_mcq_marks.setError("Please Enter MCQ Weightage");
                                                }
                                            } else {
                                                ed_mcqno.setError("Please Enter MCQ Number");
                                            }
                                        } else {
                                            progressDialog.show();
                                            if (!mainfile.getPath().equals("null") || assessment_images.size() > 0 || !mainpdf.getPath().equals("null")) {
                                                try {
                                                    uploadFile(selectedImage);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddAssessmentActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(AddAssessmentActivity.this, "Please select subject for assessment", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddAssessmentActivity.this, "Please select class for assessment", Toast.LENGTH_SHORT).show();
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

                datePickerDialog = new DatePickerDialog(AddAssessmentActivity.this,
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
                                // _ldate = yy + "-" + mm + "-" + dd;
                                _cdate2 = yy + "-" + mm + "-" + dd;
                                sdate = dd + "/" + mm + "/" + yy;
                                show_Timepicker();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year1 = calendar.get(Calendar.YEAR);
                month1 = calendar.get(Calendar.MONTH);
                day1 = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddAssessmentActivity.this,
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
                                _cdate1 = yy + "-" + mm + "-" + dd;
                                edate = dd + "/" + mm + "/" + yy;
                                show_Timepicker_end();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void show_Timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddAssessmentActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            mHour = view.getHour();
                            mMinute = view.getMinute();
                        } else {
                            mHour = view.getCurrentHour();
                            mMinute = view.getCurrentMinute();
                        }

                        choose_date.setText(_cdate2 + " " + mHour + ":" + mMinute + ":" + "00");
                        startdate = sdate + " " + mHour + ":" + mMinute + ":" + "00";
                    }
                }, mHour, mMinute, android.text.format.DateFormat.is24HourFormat(AddAssessmentActivity.this));

        timePickerDialog.show();
    }

    private void show_Timepicker_end() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddAssessmentActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            mHour1 = view.getHour();
                            mMinute1 = view.getMinute();
                        } else {
                            mHour1 = view.getCurrentHour();
                            mMinute1 = view.getCurrentMinute();
                        }
                        // mHour1 = pHour;
                        // mMinute1 = pMinute;
                        btnEnd.setText(_cdate1 + " " + mHour1 + ":" + mMinute1 + ":" + "00");
                        enddate = edate + " " + mHour1 + ":" + mMinute1 + ":" + "00";
                    }
                }, mHour1, mMinute1, android.text.format.DateFormat.is24HourFormat(AddAssessmentActivity.this));

        timePickerDialog.show();
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
        //choose_date.setText(getdate);
        _ldate = getdate;
        _cdate = getdate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, AddAssessmentActivity.this, new DefaultCallback() {
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
                        compressed = new Compressor(AddAssessmentActivity.this)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(compressed);
                    Log.d(TAG, "file_path" + imageUri);
                    assessment_images.add(String.valueOf(imageUri));
                    Log.d(TAG, "vehical images: " + assessment_images.size());
                    assessmentImagesAdapter = new AssessmentImagesAdapter(AddAssessmentActivity.this, assessment_images, AddAssessmentActivity.this);
                    layoutManager = new LinearLayoutManager(AddAssessmentActivity.this, LinearLayoutManager.HORIZONTAL, false);
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
        }
    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        galleryIntent.setData(picUri);
        this.sendBroadcast(galleryIntent);
    }


   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void getSubjects(String classid) {
        final String[][] totalSubjectsid = new String[1][1];
        Call<SubjectResponse> call = RetrofitClient.getInstance().getApi().getSubjects(classid);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if(response.body().isError() == false) {
                    List<Subjects> subjects = new ArrayList<>();
                    SubjectResponse subjectResponse = response.body();
                    subjects = subjectResponse.getSubjects();

                    if(subjects.size() > 0) {
                        //Creating an String array for the ListView
                        String[] totalSubjects = new String[subjects.size()];
                        totalSubjectsid[0] = new String[subjects.size()];

                        //looping through all the heroes and inserting the names inside the string array
                        for (int i = 0; i < subjects.size(); i++) {
                            totalSubjects[i] = subjects.get(i).getSubject_name();
                            totalSubjectsid[0][i] = subjects.get(i).getSubject_id();
                        }

                        ArrayAdapter<String> adapter2;
                        adapter2 = new ArrayAdapter<String>(AddAssessmentActivity.this, android.R.layout.simple_list_item_1, totalSubjects);
                        //setting adapter to spinner
                        spinner2.setAdapter(adapter2);
                        _subjectid = totalSubjectsid[0][0];
                    }
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
                adapter = new ArrayAdapter<String>(AddAssessmentActivity.this, android.R.layout.simple_list_item_1, totalclasses);
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
                if (InternetCheck.isInternetOn(AddAssessmentActivity.this) == true) {
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    private void uploadFile(Uri fileUri) throws IOException {
        Date date2;
        Date date1;
        File main = new File("null");
        if (rl_Images.getVisibility() == View.VISIBLE) {
        if (assessment_images.size() > 0) {
                  createPDFNew();
                  Log.d(TAG, mainpdf + " pdf");
              }
            }

        if (!mainfile.getPath().equals("null")) {
            main = new Compressor(this).compressToFile(mainfile);
        }

        Log.d(TAG, mainpdf + " " + mainfile + " " + ed_mcqno.getText().toString() + " mcqno " + ed_mcq_marks.getText().toString() + " mcqmarks");
        max = edMaximum.getText().toString();
        if (chkAssessment.isChecked()) {
            time_bound1 = "1";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            if (!startdate.equals("") && !enddate.equals("")) {
                try {
                    date1 = simpleDateFormat.parse(startdate);
                    date2 = simpleDateFormat.parse(enddate);
                    different = date2.getTime() - date1.getTime();
                    difference1 = date2.getTime() - date1.getTime();

                    Log.d(TAG, "startDate : " + date1.getTime());
                    Log.d(TAG, "endDate : " + date2.getTime());
                    Log.d(TAG, "different : " + different);

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;

                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    long elapsedSeconds = different / secondsInMilli;

                    Log.d(TAG,
                            "%d days, %d hours, %d minutes, %d seconds%n" +
                                    elapsedDays + " " + elapsedHours + " " + elapsedMinutes + " " + elapsedSeconds);

                    Log.d(TAG, difference1 + " " + different);
                    if (difference1 > 0) {
                        Log.d(TAG, choose_date.getText().toString() + "start" + btnEnd.getText().toString());
                        if (remarksitem.equalsIgnoreCase("Marks")) {
                            if (!max.equals("")) {
                                if (!mainfile.getPath().equals("null")) {
                                   requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                                }else {
                                     requestFile = RequestBody.create(MediaType.parse("image/*"),"");
                                }
                                if (rl_Images.getVisibility() == View.VISIBLE) {
                                    if (assessment_images.size() > 0) {
                                        requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                                        Log.d(TAG, mainpdf + " pdf");
                                    } else {
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
                                RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), choose_date.getText().toString());
                                RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), btnEnd.getText().toString());
                                RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), time_bound1);
                                RequestBody unit = RequestBody.create(MediaType.parse("text/plain"), remarksitem);
                                RequestBody max1 = RequestBody.create(MediaType.parse("text/plain"), max);
                                RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), ed_mcq_marks.getText().toString());
                                RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), ed_mcqno.getText().toString());
                                RequestBody negative_marks_count =  RequestBody.create(MediaType.parse("text/plain"),ed_nagitive_marking.getText().toString());

                                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count,negative_marks_count);
                                call.enqueue(new Callback<DefaultResponse>() {
                                    @Override
                                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                        DefaultResponse defaultResponse = response.body();
                                        if (!defaultResponse.isErr()) {
                                            title.setText("");
                                            description.setText("");
                                            edMaximum.setText("");
                                            lltimebound.setVisibility(View.GONE);
                                            progressDialog.dismiss();
                                            chkAssessment.setChecked(false);
                                            startActivity(new Intent(AddAssessmentActivity.this, AssessmentstaffActivity.class));
                                            Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
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
                            } else {
                                Toast.makeText(AddAssessmentActivity.this, "Please Enter Maximum marks", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (!mainfile.getPath().equals("null")) {
                                requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                            }else {
                                requestFile = RequestBody.create(MediaType.parse("image/*"),"");
                            }
                            if (rl_Images.getVisibility() == View.VISIBLE) {
                                if (assessment_images.size() > 0) {
                                    requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                                    Log.d(TAG, mainpdf + " pdf");
                                } else {
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
                            RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), choose_date.getText().toString());
                            RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), btnEnd.getText().toString());
                            RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), time_bound1);
                            RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), ed_mcq_marks.getText().toString());
                            RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), ed_mcqno.getText().toString());
                            Log.d(TAG, assessment + " assessment");
                            RequestBody negative_marks_count =  RequestBody.create(MediaType.parse("text/plain"),ed_nagitive_marking.getText().toString());

                            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count,negative_marks_count);
                            call.enqueue(new Callback<DefaultResponse>() {
                                @Override
                                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                    DefaultResponse defaultResponse = response.body();
                                    if (!defaultResponse.isErr()) {
                                        title.setText("");
                                        description.setText("");
                                        edMaximum.setText("");
                                        lltimebound.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                        chkAssessment.setChecked(false);
                                        startActivity(new Intent(AddAssessmentActivity.this, AssessmentstaffActivity.class));
                                        Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.d(TAG, t.getMessage());
                                    // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Assessment end date must be greater than start date", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please Select Start or end date", Toast.LENGTH_LONG).show();
            }
        } else {
            time_bound1 = "0";
            if (remarksitem.equalsIgnoreCase("Marks")) {
                if (!max.equals("")) {
                    if (!mainfile.getPath().equals("null")) {
                        requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                    }else {
                        requestFile = RequestBody.create(MediaType.parse("image/*"),"");
                    }
                    if (rl_Images.getVisibility() == View.VISIBLE) {
                        if (assessment_images.size() > 0) {
                            requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                            Log.d(TAG, mainpdf + " pdf");
                        } else {
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
                    RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), choose_date.getText().toString());
                    RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), btnEnd.getText().toString());
                    RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), time_bound1);
                    RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), ed_mcq_marks.getText().toString());
                    RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), ed_mcqno.getText().toString());
                    Log.d(TAG, assessment + " assessment");

                    RequestBody negative_marks_count =  RequestBody.create(MediaType.parse("text/plain"),ed_nagitive_marking.getText().toString());

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count,negative_marks_count);                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse defaultResponse = response.body();
                            Log.d(TAG , defaultResponse+"");
                            if (!defaultResponse.isErr()) {
                                title.setText("");
                                description.setText("");
                                edMaximum.setText("");
                                lltimebound.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                chkAssessment.setChecked(false);
                                startActivity(new Intent(AddAssessmentActivity.this, AssessmentstaffActivity.class));

                                Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d(TAG, t.getMessage());
                            //  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AddAssessmentActivity.this, "Please Enter Maximum marks", Toast.LENGTH_LONG).show();
                }
            } else {
                if (!mainfile.getPath().equals("null")) {
                    requestFile = RequestBody.create(MediaType.parse("image/*"), main);
                }else {
                    requestFile = RequestBody.create(MediaType.parse("image/*"),"");
                }
                if (rl_Images.getVisibility() == View.VISIBLE) {
                    if (assessment_images.size() > 0) {
                        requestPdf = RequestBody.create(MediaType.parse("application/pdf"), mainpdf);
                        Log.d(TAG, mainpdf + " pdf");
                    } else {
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
                RequestBody start_datetime = RequestBody.create(MediaType.parse("text/plain"), choose_date.getText().toString());
                RequestBody end_datetime = RequestBody.create(MediaType.parse("text/plain"), btnEnd.getText().toString());
                RequestBody time_bound = RequestBody.create(MediaType.parse("text/plain"), time_bound1);
                RequestBody mcq_marks = RequestBody.create(MediaType.parse("text/plain"), ed_mcq_marks.getText().toString());
                RequestBody mcq_count = RequestBody.create(MediaType.parse("text/plain"), ed_mcqno.getText().toString());
                Log.d(TAG, assessment + " assessment");
                RequestBody negative_marks_count =  RequestBody.create(MediaType.parse("text/plain"),ed_nagitive_marking.getText().toString());

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(requestFile, requestPdf, a_title, a_description, a_classid, a_subid, a_ldate, a_cdate, a_staffid, assessment_check, start_datetime, end_datetime, time_bound, unit, max1, mcq_marks, mcq_count,negative_marks_count);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse defaultResponse = response.body();
                        Log.d(TAG , defaultResponse+"");
                        if (!defaultResponse.isErr()) {
                            title.setText("");
                            description.setText("");
                            edMaximum.setText("");
                            lltimebound.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            chkAssessment.setChecked(false);
                            startActivity(new Intent(AddAssessmentActivity.this, AssessmentstaffActivity.class));
                            Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d(TAG, t.getMessage());
                        // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
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
                        if (InternetCheck.isInternetOn(AddAssessmentActivity.this) == true) {
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
        File  dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

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
                    }

                    else {
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

}
