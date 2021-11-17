package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.school.iqdigit.Adapter.AssessmentImagesAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.StudyAddResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.interfaces.GetClikedImagePath;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.aprilapps.easyphotopicker.Constants.DEFAULT_FOLDER_NAME;

public class AddPdfActivity extends AppCompatActivity implements GetClikedImagePath {
    private EditText ed_title, ed_lesson;
    private TextView upload_file;
    private CheckBox chkPages;
    private RelativeLayout rl_Images;
    private TextView text_no_available;
    private ArrayList<String> assessment_images;
    private Uri imageUri;
    private RecyclerView recyclerView_pic;
    private File mainpdf = new File("null");
    private String TAG = "AddPdfActivity";
    private String pictureFilePath;
    private AssessmentImagesAdapter assessmentImagesAdapter;
    private LinearLayoutManager layoutManager;
    private File mainfile = new File("null");;
    private String _staffid;
    private int STORAGE_PERMISSION = 1;
    private Intent mainintent;
    private String chapterid = "",subject_id="",chapter_name="",action ="";
    private String title = "" ,description = "",running_id = "";
    private Button btnSubTutorial;
    private File main = new File("null");
    private ProgressDialog progressDialog;
    private FloatingActionButton img_pic;
    private ImageView backbtn;
    private Uri filePath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        ed_title = findViewById(R.id.ed_book_title);
        ed_lesson = findViewById(R.id.ed_lesson);
        backbtn = findViewById(R.id.backbtn);
        recyclerView_pic = findViewById(R.id.recycleView_pic);
        img_pic = findViewById(R.id.img_click_pic);
        assessment_images = new ArrayList<>();
        text_no_available = findViewById(R.id.text_no_available);
        upload_file = findViewById(R.id.upload_btn);
        chkPages = findViewById(R.id.chkPages);
        rl_Images = findViewById(R.id.rl_Images);
        btnSubTutorial = findViewById(R.id.btnSubTutorial);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name_main));
        progressDialog.setMessage("Loading....");
        progressDialog.create();
        mainintent = getIntent();
        title = mainintent.getStringExtra("title");
        description = mainintent.getStringExtra("lesson");
        running_id = mainintent.getStringExtra("running_id");
        action = mainintent.getStringExtra("action");
        chapterid = mainintent.getStringExtra("chapterid");
        subject_id = mainintent.getStringExtra("subject_id");
        chapter_name = mainintent.getStringExtra("chapter_name");
        Log.d(TAG , "chapterid "+chapterid+"subjectid"+subject_id);
        if(action.equals("edit"))
        {
            ed_title.setText(title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ed_lesson.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                ed_lesson.setText(Html.fromHtml(description));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Staff staff = SharedPrefManager2.getInstance(AddPdfActivity.this).getStaff();
        _staffid = staff.getId();
        if ((ContextCompat.checkSelfPermission(AddPdfActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(AddPdfActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(AddPdfActivity.this, Manifest.permission.CAMERA)
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
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 1);
               /*new MaterialFilePicker()
                       .withActivity(AddPdfActivity.this)
                       .withRequestCode(1)
                       .withHiddenFiles(true)
                       .withFilter(Pattern.compile(".*\\.pdf$"))
                       .withTitle("Select PDF File")
                       .start();*/
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

        btnSubTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_title.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!ed_lesson.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!main.getPath().equals("null") || assessment_images.size()>0) {
                            if (action.equals("add")) {
                                uploadBooklet();
                            } else if (action.equals("edit")) {
                                uploadEditedBooklet();
                            }
                        } else{
                            Toast.makeText(getApplicationContext(), "Please select Image", Toast.LENGTH_LONG).show();}
                    } else {
                        ed_lesson.setError("Please enter Book Description");
                    }

                } else {
                    ed_title.setError("Please enter Book Title");
                }
            }
        });
        img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(AddPdfActivity.this, "", 0);
            }
        });
    }

    private void uploadEditedBooklet() {
        progressDialog.show();
        if(rl_Images.getVisibility() == View.VISIBLE)
        {
            createPDFNew();
        }
        Log.d(TAG , "1"+subject_id+"2"+ed_title.getText().toString()+"3"+main+"4"+chapterid+"5 "+ed_lesson.getText().toString()+"6 "+_staffid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"),subject_id );
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), main);
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"),ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"),"active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);
        RequestBody a_running_id = RequestBody.create(MediaType.parse("text/plain"), running_id);

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().edit_lms_pdf(a_subject_id,a_book_name,requestFile,a_chapter_id,a_book_desc,a_status,a_user_type, a_user_id,a_running_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG , " response: "+response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddPdfActivity.this,StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid",chapterid);
                    intent.putExtra("subject_id",subject_id);
                    intent.putExtra("chaptername",chapter_name);
                    intent.putExtra("type","pdf");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Pdf Uploaded Successfully", Toast.LENGTH_LONG).show();
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
        if(rl_Images.getVisibility() == View.VISIBLE)
        {
            createPDFNew();
        }
        Log.d(TAG , "1"+subject_id+"2"+ed_title.getText().toString()+"3"+main+"4"+chapterid+"5 "+ed_lesson.getText().toString()+"6 "+_staffid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"),subject_id );
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), main);
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"),ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"),"active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().add_lms_pdf(a_subject_id,a_book_name,requestFile,a_chapter_id,a_book_desc,a_status,a_user_type, a_user_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG , " response: "+response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddPdfActivity.this,StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid",chapterid);
                    intent.putExtra("subject_id",subject_id);
                    intent.putExtra("chaptername",chapter_name);
                    intent.putExtra("type","pdf");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Pdf Uploaded Successfully", Toast.LENGTH_LONG).show();
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

    static File pickedExistingPicture(@NonNull Context context, Uri photoUri) throws IOException {
        InputStream pictureInputStream = context.getContentResolver().openInputStream(photoUri);
        File directory = tempImageDirectory(context);
        File photoFile = new File(directory, UUID.randomUUID().toString() + "." + getMimeType(context, photoUri));
        photoFile.createNewFile();
        writeToFile(pictureInputStream, photoFile);
        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, AddPdfActivity.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File compressed = null;
                try {
                    compressed = new Compressor(AddPdfActivity.this)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(imageFiles.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri =  Uri.fromFile(compressed);
                Log.d(TAG, "file_path" + imageUri);
                assessment_images.add(String.valueOf(imageUri));
                Log.d(TAG, "vehical images: " + assessment_images.size());
                assessmentImagesAdapter = new AssessmentImagesAdapter(AddPdfActivity.this, assessment_images, AddPdfActivity.this);
                layoutManager = new LinearLayoutManager(AddPdfActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView_pic.setLayoutManager(layoutManager);
                recyclerView_pic.setAdapter(assessmentImagesAdapter);
                text_no_available.setVisibility(View.GONE);
            }
        });
        if (resultCode == RESULT_OK && requestCode == 1) {
            filePath = data.getData();

            try {
                main = pickedExistingPicture(this, filePath);
                if (upload_file.getVisibility() == View.VISIBLE) {
                    upload_file.setText("pdf Selected");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG ,filePath + "filepath");
            Log.d(TAG ,main + "MainFile");

           /* // Get the Uri of the selected file
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                Log.d(TAG , filePath);
                main = new File(filePath);
                if (upload_file.getVisibility() == View.VISIBLE) {
                    upload_file.setText("pdf Selected: " + filePath);
                }*/
        }
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
        //progressDialog.show();
        File  dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        if (!dir.exists())
            dir.mkdirs();
        main = new File(dir, "assessment.pdf");
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
