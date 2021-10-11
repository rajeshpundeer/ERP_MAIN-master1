package com.school.iqdigit.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.ExpandableListAdapter;
import com.school.iqdigit.Adapter.ExpandableListStaffAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Chapters;
import com.school.iqdigit.Model.ChaptersResponse;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Model.Units;
import com.school.iqdigit.Model.UnitsResponse;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utils.SharedHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMaterialStaff1Activity extends AppCompatActivity {
    private ProgressDialog mProg;
    private ImageView backbtn;
    private String TAG = "StudyMaterialActivity";
    private String _subjectid = "";
    ExpandableListStaffAdapter listAdapter;
    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    private ArrayList<Chapters> chaptersListnew = new ArrayList<>();
    private ArrayList<Units> unitsListnew = new ArrayList<>();
    ArrayList<String> units = new ArrayList<>();
    ArrayList<String> hashmapList = new ArrayList<>();
    private String _classid = "";
    private String subjectname="",classname="";
    private Button btnAddUnit;
    private Intent intent;
    private TextView tvtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_staff1);
        backbtn = findViewById(R.id.backbtn);
        tvtitle = findViewById(R.id.tvtitle);
        intent = getIntent();
        _classid =    intent.getStringExtra("classid");
        _subjectid = intent.getStringExtra("subjectid");
        classname = intent.getStringExtra("classname");
        subjectname = intent.getStringExtra("subjectname");
        tvtitle.setText(classname+"["+subjectname+"]");

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        btnAddUnit = findViewById(R.id.btnAddUnit);
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        if (InternetCheck.isInternetOn(StudyMaterialStaff1Activity.this) == true) {
            getUnits(_subjectid);
        } else {
            showsnackbar();
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Showaddunitpopup();
            }
        });
    }

    private void getUnits(String subjectid) {
        mProg.show();
        Call<UnitsResponse> call = RetrofitClient.getInstance().getApi().getunits(_classid, _subjectid);
        // Call<UnitsResponse> call = RetrofitClient.getInstance().getApi().getunits("4", "8");
        call.enqueue(new Callback<UnitsResponse>() {
            @Override
            public void onResponse(Call<UnitsResponse> call, Response<UnitsResponse> response) {
                if (!response.body().equals(null)) {
                    if (response.body().isError() == false) {
                        List<Units> unitsList = new ArrayList<>();
                        unitsList.clear();
                        unitsListnew.clear();
                        units.clear();
                        UnitsResponse unitsResponse = response.body();
                        unitsList = unitsResponse.getUnits();
                        if (unitsList.size() <= 0) {
                            mProg.dismiss();
                            Toast.makeText(getApplicationContext(), "Study Material Not Available for this Subject", Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, unitsList.size() + " unitsList");

                        for (int i = 0; i < unitsList.size(); i++) {
                            unitsListnew.add(new Units(unitsList.get(i).getUnit_id(), unitsList.get(i).getUnit_name()));
                            String unitname = new String(unitsList.get(i).getUnit_name());
                            Log.d(TAG , unitname+"^"+String.valueOf(unitsListnew.get(i).getUnit_id())+" unitlist");
                            units.add(unitname+"^"+String.valueOf(unitsListnew.get(i).getUnit_id()));
                        }
                        //getchapters("8");
                        getchapters(subjectid);
                    } else {
                        Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UnitsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                mProg.dismiss();
            }
        });
    }

    private void getchapters(String _subjectid) {
        Call<ChaptersResponse> call = RetrofitClient.getInstance().getApi().getchapters(_classid, _subjectid);
        //Call<ChaptersResponse> call = RetrofitClient.getInstance().getApi().getchapters("4", "8");
        call.enqueue(new Callback<ChaptersResponse>() {
            @Override
            public void onResponse(Call<ChaptersResponse> call, Response<ChaptersResponse> response) {
                if (!response.body().equals(null)) {
                    if (response.body().isError() == false) {
                        List<Chapters> chaptersList = new ArrayList<>();
                        chaptersList.clear();
                        chaptersListnew.clear();
                        hashmapList.clear();
                        ChaptersResponse chaptersResponse = response.body();
                        chaptersList = chaptersResponse.getChapters();

                        Log.d(TAG, chaptersList.size() + " chaptersList");
                        //looping through all the heroes and inserting the names inside the string array
                        for (int i = 0; i < chaptersList.size(); i++) {
                            //String chapter_id,String chapter_name,String unit_id,String unit_name,String classesid
                            chaptersListnew.add(new Chapters(chaptersList.get(i).getChapter_id(), chaptersList.get(i).getChapter_name(),
                                    chaptersList.get(i).getUnit_id(), chaptersList.get(i).getUnit_name(), chaptersList.get(i).getClassesid()));
                        }

                        for (int i = 0; i < unitsListnew.size(); i++) {
                            hashmapList = new ArrayList<>();
                            for (int j = 0; j < chaptersListnew.size(); j++) {
                                Log.d(TAG, j + "j values " + unitsListnew.get(i).getUnit_id() + " unit list " + chaptersListnew.get(j).getUnit_id());
                                if (unitsListnew.get(i).getUnit_id().equals(chaptersListnew.get(j).getUnit_id())) {
                                    hashmapList.add(chaptersListnew.get(j).getChapter_name() + "^" + chaptersListnew.get(j).getChapter_id());
                                }
                            }
                            Log.d(TAG, unitsListnew.get(i).getUnit_id() + " unitid");
                            listDataChild.put(units.get(i), hashmapList);
                        }

                        Log.d(TAG, "hashmap list size " + listDataChild.size() + " unitlist " + units.size());
                        listAdapter = new ExpandableListStaffAdapter(StudyMaterialStaff1Activity.this, units, listDataChild,_subjectid);
                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        mProg.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChaptersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Study Material Not Available", Toast.LENGTH_LONG).show();
                mProg.dismiss();
            }
        });
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
                        if (InternetCheck.isInternetOn(StudyMaterialStaff1Activity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    public void Showaddunitpopup() {
        Dialog dialog;
        FloatingActionButton imgcancel;
        Button btnOk;
        EditText enterunit;
        dialog = new Dialog(StudyMaterialStaff1Activity.this);
        dialog.setContentView(R.layout.dialog_addunit);
        imgcancel = dialog.findViewById(R.id.bt_close);
        btnOk = dialog.findViewById(R.id.btnok);
        enterunit = dialog.findViewById(R.id.ed_addunit);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enterunit.getText().toString().equals("")) {
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setadd_unit(_classid, _subjectid, enterunit.getText().toString());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            mProg.dismiss();
                            if (response.body() != null) {
                                if (response.body().isErr() == false) {
                                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                } else {
                                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            mProg.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Please Enter Unit Name before submit", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
    }
}
