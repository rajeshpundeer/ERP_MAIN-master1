package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.ExpandableListAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Chapters;
import com.school.iqdigit.Model.ChaptersResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Model.Units;
import com.school.iqdigit.Model.UnitsResponse;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMaterialActivity extends AppCompatActivity {
    private String classid;
    private ProgressDialog mProg;
    private ImageView backbtn;
    private String TAG = "StudyMaterialActivity";
    private Spinner spSubject;
    private String _subjectid = "";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    private ArrayList<Chapters> chaptersListnew = new ArrayList<>();
    private ArrayList<Units> unitsListnew = new ArrayList<>();
    ArrayList<String> units = new ArrayList<>();
    ArrayList<String> hashmapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);
        spSubject = findViewById(R.id.spSubject);
        backbtn = findViewById(R.id.backbtn);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();

        final User user = SharedPrefManager.getInstance(this).getUser();
        Log.d(TAG, user.getId() + " userid");
        // Get Data of User to show at Header of This page.
        if (InternetCheck.isInternetOn(StudyMaterialActivity.this) == true) {
            Call<ProfileResponse> call2 = RetrofitClient
                    .getInstance().getApi().getUserProfile(user.getId());
            call2.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    ProfileResponse profileResponse = response.body();
                    Log.d(TAG, profileResponse.getUserprofile().getClassid() + " classid");
                    classid = profileResponse.getUserprofile().getClassid();
                    getSubjects(classid);
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    if (mProg.isShowing()) {
                        mProg.dismiss();
                    }
                    //Toast.makeText(Examination.this, "Error :"+ t , Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            showsnackbar();
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getSubjects(String classid) {
        final String[][] totalSubjectsid = new String[1][1];
        Call<SubjectResponse> call = RetrofitClient.getInstance().getApi().getSubjects_lms(classid);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body().isError() == false) {
                    List<Subjects> subjects;
                    SubjectResponse subjectResponse = response.body();
                    subjects = subjectResponse.getSubjects();
                    Log.d(TAG, subjects + " subjects list");
                    //Creating an String array for the ListView
                    String[] totalSubjects = new String[subjects.size()];
                    totalSubjectsid[0] = new String[subjects.size()];

                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < subjects.size(); i++) {
                        Log.d(TAG, subjects.get(i).getSubject_name() + " subjects Name");
                        totalSubjects[i] = subjects.get(i).getSubject_name();
                        totalSubjectsid[0][i] = subjects.get(i).getSubject_id();
                    }

                    ArrayAdapter<String> adapter2;
                    adapter2 = new ArrayAdapter<String>(StudyMaterialActivity.this, android.R.layout.simple_list_item_1, totalSubjects);
                    //setting adapter to spinner
                    spSubject.setAdapter(adapter2);
                    _subjectid = totalSubjectsid[0][0];
                    getUnits(_subjectid);
                    //  getUnits("8");
                    mProg.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Subjects Not Avalable", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Subjects Not Avalable", Toast.LENGTH_LONG).show();
            }
        });
        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String subbjectid = totalSubjectsid[0][position];
                _subjectid = subbjectid;
                if (InternetCheck.isInternetOn(StudyMaterialActivity.this) == true) {
                    //getUnits("8");
                    getUnits(_subjectid);
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      /*  spSubject.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                String subbjectid = totalSubjectsid[0][position];
                _subjectid = subbjectid;
                if (InternetCheck.isInternetOn(StudyMaterialActivity.this) == true) {
                    //getUnits("8");
                    getUnits(_subjectid);
                } else {
                    showsnackbar();
                }
            }
        });*/
    }

    private void getUnits(String subjectid) {
        mProg.show();
        Call<UnitsResponse> call = RetrofitClient.getInstance().getApi().getunits(classid, _subjectid);
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
                        if(unitsList.size() > 0) {
                            Log.d(TAG, unitsList.size() + " unitsList");

                            for (int i = 0; i < unitsList.size(); i++) {
                                unitsListnew.add(new Units(unitsList.get(i).getUnit_id(), unitsList.get(i).getUnit_name()));
                                String unitname = new String(unitsList.get(i).getUnit_name());
                                units.add(unitname);
                            }
                            //getchapters("8");
                            getchapters(subjectid);
                        }else {
                            mProg.dismiss();
                            Toast.makeText(getApplicationContext(), "Study Material Not Available for this Subject", Toast.LENGTH_LONG).show();
                        }
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
        Call<ChaptersResponse> call = RetrofitClient.getInstance().getApi().getchapters(classid, _subjectid);
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
                                    hashmapList.add(chaptersListnew.get(j).getChapter_name() + "_" + chaptersListnew.get(j).getChapter_id());
                                }
                            }
                            Log.d(TAG, unitsListnew.get(i).getUnit_id() + " unitid");
                            listDataChild.put(units.get(i), hashmapList);
                        }

                        Log.d(TAG, "hashmap list size " + listDataChild.size() + " unitlist " + units.size());
                        listAdapter = new ExpandableListAdapter(StudyMaterialActivity.this, units, listDataChild);
                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        mProg.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Study Matrial Not Available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Study Matrial Not Available", Toast.LENGTH_LONG).show();
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
                        if (InternetCheck.isInternetOn(StudyMaterialActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
