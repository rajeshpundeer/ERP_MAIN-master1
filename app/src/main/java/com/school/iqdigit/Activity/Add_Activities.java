package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.SingleSelectAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GroupsResponse;
import com.school.iqdigit.Model.StudgroupsItem;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.google.gson.Gson;
import com.school.iqdigit.interfaces.SingleItemClick;
import com.school.iqdigit.utility.InternetCheck;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Activities extends AppCompatActivity {
    private List<Classes> classes;
    private ArrayList<Classes> classesArrayList = new ArrayList<Classes>();
    // private TextView spinnerClass;
    private TextView spinner,spSelectGroup;
    private EditText title, description;
    private TextView upload_file;
    private Button choose_date;
    private ImageView submit, backbtn;
    private Uri selectedImage;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ProgressDialog progressDialog;
    private int STORAGE_PERMISSION = 1;
    private int CAMERA_PERMISSION = 11;
    private CheckBox allclasscheck;
    private String[][] totalclassesid;
    private Staff staff;
    File mainfile;
    int year;
    int month;
    int day;
    String mCurrentPhotoPath;
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 100;
    private String pictureFilePath;
    MultiAdapter multiAdapter;
    //Submit data elements
    private String _title = "", _description = "", _ldate = "", _cdate = "", _classid = "", _subjectid = "", _staffid = "";
    private boolean checkinternet;
    private StringBuilder classesname = new StringBuilder();
    private StringBuilder classesid = new StringBuilder();
    private LinearLayout select_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activities);
        staff = SharedPrefManager2.getInstance(Add_Activities.this).getStaff();
        totalclassesid = new String[1][1];
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Activities");
        progressDialog.setMessage("Wait a moment while \nActivity is Uploading....");
        progressDialog.create();
        title = findViewById(R.id.title_sa);
        description = findViewById(R.id.description_sa);
        upload_file = findViewById(R.id.upload_btn);
        submit = findViewById(R.id.next);
        submit.setVisibility(View.GONE);
        backbtn = findViewById(R.id.backbtn);
        spinner = findViewById(R.id.spCompany);
        select_group = findViewById(R.id.select_group);
        spSelectGroup = findViewById(R.id.spSelectGroup);
        //  spinnerClass =  findViewById(R.id.spinnerClass);
        allclasscheck = findViewById(R.id.checkallclasses);
        checkinternet = InternetCheck.isInternetOn(Add_Activities.this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Activities.this, MainActivity.class);
                intent.putExtra("usertype", "staff");
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(Add_Activities.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assignment")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Add_Activities.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
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
        if (ContextCompat.checkSelfPermission(Add_Activities.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for upload Assignment")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Add_Activities.this, new String[]{Manifest.permission.CAMERA
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
        if (InternetCheck.isInternetOn(Add_Activities.this) == true) {
            getClasses();
            getGroup();
        } else {
            showsnackbar();
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(AssignmentStaff.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            finish();
//            startActivity(intent);
//            return;
//        }
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _title = title.getText().toString();
                _description = description.getText().toString();
                if (!_title.equals("")) {
                    if (!_description.equals("")) {
                        if (!_classid.equals("")) {
                            EasyImage.openChooserWithGallery(Add_Activities.this, "", 0);
                        } else {
                            Toast.makeText(Add_Activities.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
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
                _staffid = staff.getId();
                _title = title.getText().toString();
                _description = description.getText().toString();
                if (!title.equals(null)) {
                    if (!description.equals(null)) {
                        if (!_ldate.equals(null) && !_cdate.equals(null)) {
                            if (!_classid.equals(null)) {
                                progressDialog.show();
                                try {
                                    if (checkinternet == true) {
                                        uploadFile(selectedImage);
                                    } else {
                                        showsnackbar();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(Add_Activities.this, "Please select class for assignment", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Add_Activities.this, "Please choose date for assignment", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        description.setError("Please enter description for assignment");
                    }
                } else {
                    title.setError("Please enter title for assignment");
                }
            }
        });

    }

    private void getGroup() {

        Call<GroupsResponse> call = RetrofitClient.getInstance().getApi().getGroup();
        call.enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {


                if(response.code() ==200){



                    if(!response.body().getStudgroups().isEmpty()){
                        select_group.setVisibility(View.VISIBLE);
                        spSelectGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                showGroupDialog(response.body().getStudgroups());
                            }
                        });
                    }else{
                        select_group.setVisibility(View.GONE);
                    }
                }




            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {

            }
        });

    }

    StudgroupsItem item;
    private void showGroupDialog(List<StudgroupsItem> studgroups) {

        Dialog dialog = new Dialog(Add_Activities.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_recycler);
        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        Button btncancel = (Button) dialog.findViewById(R.id.btnCancel);
        RecyclerView recyclerView = dialog.findViewById(R.id.rvClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new SingleSelectAdapter(studgroups, Add_Activities.this, new SingleItemClick() {
            @Override
            public void click(StudgroupsItem studgroupsItem) {
                item = studgroupsItem;
                //Toast.makeText(Add_Activities.this, ""+studgroupsItem.getGroupName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(item !=null){
                    spSelectGroup.setText(item.getGroupName());
                }else {
                    spSelectGroup.setText("Select Group");
                }

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = null;
                spSelectGroup.setText("Select Group");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, Add_Activities.this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                upload_file.setText("Image Selected");
                submit.setVisibility(View.VISIBLE);
                mainfile = imageFiles.get(0);

            }
        });

    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        galleryIntent.setData(picUri);
        this.sendBroadcast(galleryIntent);
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
        } else if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getClasses() {
        Call<ClassResponse> call = RetrofitClient.getInstance().getApi().getClasses(staff.getId());
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {


                ClassResponse classResponse = response.body();

                classes = classResponse.getClasses();

                //Creating an String array for the ListView
                String[] totalclasses = new String[classes.size()];
                totalclassesid[0] = new String[classes.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < classes.size(); i++) {
                    totalclasses[i] = classes.get(i).getClass_name();
                    totalclassesid[0][i] = classes.get(i).getClass_id();
                    Log.v("class", totalclassesid[0][i]);
                    classesArrayList.add(new Classes(classes.get(i).getClass_id(), classes.get(i).getClass_name(), false));
                }

                // ArrayAdapter<String> adapter;
                //adapter = new ArrayAdapter<String>(Add_Activities.this, android.R.layout.simple_list_item_1, totalclasses);
                // setting adapter to spinner
                //spinner.setAdapter(adapter);
                _classid = totalclassesid[0][0];
                spinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(Add_Activities.this);
                    }
                });
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {

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
        String ischecked = "0";

        File main = new Compressor(this).compressToFile(mainfile);
        String mainfilestring = mainfile.toString();
        Log.v(TAG, mainfile.toString());
        //creating request body for
        if (allclasscheck.isChecked())
            ischecked = "1";
        else
            ischecked = "0";
        String classid1 = classesid.toString();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), main);
        RequestBody a_title = RequestBody.create(MediaType.parse("text/plain"), _title);
        RequestBody a_description = RequestBody.create(MediaType.parse("text/plain"), _description);
        RequestBody a_classid = RequestBody.create(MediaType.parse("text/plain"), classid1);
        RequestBody a_staffid = RequestBody.create(MediaType.parse("text/plain"), _staffid);
        RequestBody a_ischecked = RequestBody.create(MediaType.parse("text/plain"), ischecked);
        RequestBody studgroupid = RequestBody.create(MediaType.parse("text/plain"), item==null ? "" :String.valueOf(item.getId()));

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadactivity(requestFile, a_title, a_description, a_classid, a_staffid, a_ischecked,studgroupid);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse defaultResponse = response.body();
                if (!defaultResponse.isErr()) {
                    spinner.setText("");
                    title.setText("");
                    description.setText("");
                    progressDialog.dismiss();
                    startActivity(new Intent(Add_Activities.this,ActivitiesStaff.class));
                    finish();
                    Toast.makeText(getApplicationContext(), "Activity Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


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

    public void showDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_recycler);
        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        Button btncancel = (Button) dialog.findViewById(R.id.btnCancel);
        RecyclerView recyclerView = dialog.findViewById(R.id.rvClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiAdapter = new MultiAdapter(Add_Activities.this, classesArrayList);
        recyclerView.setAdapter(multiAdapter);

        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classesname.setLength(0);
                classesid.setLength(0);
                for (int i = 0; i < multiAdapter.getSelected().size(); i++) {
                    //insert selected classes id's in arraylist
                    if (i == multiAdapter.getSelected().size() - 1) {
                        classesid.append(multiAdapter.getSelected().get(i).getClass_id());
                        classesname.append(multiAdapter.getSelected().get(i).getClass_name());
                    } else {
                        classesid.append(multiAdapter.getSelected().get(i).getClass_id() + ",");
                        classesname.append(multiAdapter.getSelected().get(i).getClass_name() + ",");
                    }
                    String classid = totalclassesid[0][i];
                    _classid = classid;

                }
                Log.d(TAG, _classid + " class id's");
                spinner.setText(classesname);
                dialog.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                        if (InternetCheck.isInternetOn(Add_Activities.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    public class MultiAdapter extends RecyclerView.Adapter<MultiAdapter.MultiViewHolder> {
        private Context context;
        private ArrayList<Classes> classes;

        public MultiAdapter(Context context, ArrayList<Classes> classes) {
            this.context = context;
            this.classes = classes;
        }

        public void setClasses(ArrayList<Classes> classes) {
            this.classes = new ArrayList<>();
            this.classes = classes;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.class_recyclerview, viewGroup, false);
            return new MultiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
            multiViewHolder.bind(classes.get(position));
        }

        @Override
        public int getItemCount() {
            return classes.size();
        }

        class MultiViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView imageView;

            MultiViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                imageView = itemView.findViewById(R.id.imageView);
            }

            void bind(final Classes classes) {
                imageView.setVisibility(classes.isChecked() ? View.VISIBLE : View.GONE);
                textView.setText(classes.getClass_name());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        classes.setChecked(!classes.isChecked());
                        imageView.setVisibility(classes.isChecked() ? View.VISIBLE : View.GONE);
                    }
                });
            }
        }

        public ArrayList<Classes> getAll() {
            return classes;
        }

        public ArrayList<Classes> getSelected() {
            ArrayList<Classes> selected = new ArrayList<>();
            selected.clear();
            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).isChecked()) {
                    selected.add(classes.get(i));
                }
            }
            return selected;
        }
    }
}
