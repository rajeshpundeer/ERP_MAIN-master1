package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.LiveClassAttenadapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.Model.LiveAttendanceClas;
import com.school.iqdigit.Model.LiveStaffAttenResponse;
import com.school.iqdigit.Model.StudentsResponse;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Students;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceStaff1 extends AppCompatActivity implements View.OnClickListener {
    private Bundle bundle;
    private String classid, subjectid, date;
    private String TAG = "AttendanceStaff1";
    private Button btnSubmitAtnd;
    private RecyclerView rvAttendance;
    private ImageView imgBack;
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> attendance = new ArrayList<>();
    public ArrayList<String> stdAttendance = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LinkedHashMap<String, Object> mapAttendanceData;
    private TextView navigation_live_class, navigation_ragular;
    private LinearLayout view_live_class, view_ragular, ll_live, ll_Regular,noattendance,liveattenHead;
    private RecyclerView rv_liveattendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_staff1);
        bundle = getIntent().getExtras();
        classid = bundle.getString("classid");
        subjectid = bundle.getString("subjectid");
        date = bundle.getString("date");
        Log.d(TAG, classid + " " + subjectid + " " + date);
        imgBack = findViewById(R.id.backbtn1);
        imgBack.setOnClickListener(this);
        btnSubmitAtnd = findViewById(R.id.btnSubmitAtnd);
        rvAttendance = findViewById(R.id.rvAttendance);
        mapAttendanceData = new LinkedHashMap<String, Object>();

        navigation_live_class = findViewById(R.id.navigation_live_class);
        navigation_ragular = findViewById(R.id.navigation_ragular);
        view_live_class = findViewById(R.id.view_live_class);
        view_ragular = findViewById(R.id.view_ragular);
        ll_live = findViewById(R.id.ll_live);
        ll_Regular = findViewById(R.id.llRagular);
        rv_liveattendance = findViewById(R.id.rv_liveattendance);
        rv_liveattendance.setLayoutManager(new LinearLayoutManager(this));
        liveattenHead = findViewById(R.id.liveattenHead);
        noattendance = findViewById(R.id.noattendance);

        btnSubmitAtnd.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendance");
        progressDialog.setMessage("Wait a moment while \nattendance is Uploading....");
        progressDialog.create();
        if (InternetCheck.isInternetOn(AttendanceStaff1.this) == true) {
            addStudentname(classid,date);
        } else {
            showsnackbar();
        }

        navigation_ragular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noattendance.setVisibility(View.GONE);
                ll_Regular.setVisibility(View.VISIBLE);
                ll_live.setVisibility(View.GONE);
                view_ragular.setVisibility(View.VISIBLE);
                view_live_class.setVisibility(View.GONE);
            }
        });
        navigation_live_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveAttendance();
                ll_live.setVisibility(View.VISIBLE);
                ll_Regular.setVisibility(View.GONE);
                view_live_class.setVisibility(View.VISIBLE);
                view_ragular.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitAtnd: {
                progressDialog.show();
                final Staff staff = SharedPrefManager2.getInstance(AttendanceStaff1.this).getStaff();
                Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().submittattendance(
                        classid, date, subjectid, id, stdAttendance, name,staff.getId());
                call.enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        if (!response.body().isError()) {
                            progressDialog.dismiss();
                            Toast.makeText(AttendanceStaff1.this, "Attendance Submitted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AttendanceStaff1.this, "Attendance Submittion Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.backbtn1: {
                onBackPressed();
            }
        }
    }

    private void addStudentname(String classid,String attdate) {
        progressDialog.show();
        Call<StudentsResponse> call = RetrofitClient.getInstance().getApi().getStudentsforattendance(classid,attdate);
        call.enqueue(new Callback<StudentsResponse>() {
            @Override
            public void onResponse(Call<StudentsResponse> call, Response<StudentsResponse> response) {
                progressDialog.dismiss();
                List<Students> students;
                    if (response.body().getStudents().size() > 0) {
                        StudentsResponse studentsResponse = response.body();
                        students = studentsResponse.getStudents();

                        //looping through all the heroes and inserting the names inside the string array
                        for (int i = 0; i < students.size(); i++) {
                            name.add(students.get(i).getStu_name());
                            id.add(students.get(i).getStu_id());
                            //loop list<Student>  & fill mapValue
                            mapAttendanceData.put(students.get(i).getStu_id(), "P");
                            stdAttendance.add("P");
                        }
                        Log.d(TAG, stdAttendance.size() + " stdentlist size");
                        addAttendanceFormat();
                        rvAttendance.setLayoutManager(new LinearLayoutManager(AttendanceStaff1.this));
                        AttendanceStaffAdapter adapter = new AttendanceStaffAdapter(AttendanceStaff1.this, name, id, attendance);
                        rvAttendance.setAdapter(adapter);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"No Students Available",Toast.LENGTH_LONG).show();
                    }
                //}/*else {
                  /*  progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"No Students Available",Toast.LENGTH_LONG).show();
*/
                //}
            }

            @Override
            public void onFailure(Call<StudentsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void addAttendanceFormat() {
        attendance.add("P");
        attendance.add("A");
        attendance.add("L");
        attendance.add("LP");
        attendance.add("H");
    }

    //Attendance RecyclerView Adapter
    private class AttendanceStaffAdapter extends RecyclerView.Adapter<AttendanceStaffAdapter.AttendanceStaffholder> {
        private Context mCtx;
        private ArrayList<String> studentname = new ArrayList<>();
        private ArrayList<String> id = new ArrayList<>();
        private ArrayList<String> sppinerList = new ArrayList<>();
        Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();

        public AttendanceStaffAdapter(Context mCtx, ArrayList<String> studentname, ArrayList<String> id, ArrayList<String> sppinerList) {
            this.mCtx = mCtx;
            this.studentname = studentname;
            this.id = id;
            this.sppinerList = sppinerList;
        }

        @NonNull
        @Override
        public AttendanceStaffholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.rv_attendance, viewGroup, false);
            return new AttendanceStaffholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AttendanceStaffholder attendanceStaffholder, int i) {
            attendanceStaffholder.tvStdName.setText(studentname.get(i).toString());
            ArrayAdapter<String> adapter;
            adapter  = new ArrayAdapter<String>(
                    mCtx, R.layout.spinneritem, sppinerList){
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextSize(getResources().getDimension(R.dimen.sevensp));
                    if(position == 0) {
                        // Set the item text color
                        tv.setTextColor(Color.parseColor("#1B5E20"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    }
                    else if(position == 1){
                        // Set the alternate item text color
                        tv.setTextColor(Color.parseColor("#FF3D00"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    } else if(position == 2){
                        tv.setTextColor(Color.parseColor("#0D47A1"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }else if(position == 3){
                        tv.setTextColor(Color.parseColor("#AA00FF"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }else if(position == 4){
                        tv.setTextColor(Color.parseColor("#DC143C"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            //setting adapter to spinner
            attendanceStaffholder.spAttendance.setAdapter(adapter);
            if (mSpinnerSelectedItem.containsKey(i)) {
                attendanceStaffholder.spAttendance.setSelection(mSpinnerSelectedItem.get(i));
            }
            attendanceStaffholder.spAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String text = attendanceStaffholder.spAttendance.getSelectedItem().toString();
                    ((TextView)  adapterView.getChildAt(0)).setTextSize(getResources().getDimension(R.dimen.sevensp));

                   /* if(text.equals("A")){
                        ((TextView)  adapterView.getChildAt(0)).setTextColor(getColor(R.color.orange_800));
                    }
                    if(text.equals("L")){
                        ((TextView)  adapterView.getChildAt(0)).setTextColor(getColor(R.color.blue_800));
                    }if(text.equals("LP")){
                        ((TextView)  adapterView.getChildAt(0)).setTextColor(getColor(R.color.purple_A700));
                    }if(text.equals("H")){
                        ((TextView)  adapterView.getChildAt(0)).setTextColor(getColor(R.color.red_A800));
                    }*/
                    mSpinnerSelectedItem.put(i, position);
                    Log.d(TAG, "id " + id.get(i) +" "+id.indexOf(id.get(i)));
                    if(stdAttendance.size()!=0){
                        stdAttendance.set(id.indexOf(id.get(i)) , text);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return id.size();
        }

        class AttendanceStaffholder extends RecyclerView.ViewHolder {
            TextView tvStdName;
            Spinner spAttendance;
            LinearLayout layoutAttendance;

            public AttendanceStaffholder(@NonNull View itemView) {
                super(itemView);
                tvStdName = itemView.findViewById(R.id.tvStdName);
                spAttendance = itemView.findViewById(R.id.spinnerAttendance);
                layoutAttendance = itemView.findViewById(R.id.layoutAttendance);
            }
        }

    }

    private void showLiveAttendance() {
        if (InternetCheck.isInternetOn(AttendanceStaff1.this) == true) {
            progressDialog.show();
            liveattenHead.setVisibility(View.GONE);

         /*   Call<LiveStaffAttenResponse> call = RetrofitClient
                    .getInstance().getApi().getLiveStaffAttenResponse( "26","2020-06-08");*/
            Call<LiveStaffAttenResponse> call = RetrofitClient
                    .getInstance().getApi().getLiveStaffAttenResponse(classid,date);
            call.enqueue(new Callback<LiveStaffAttenResponse>() {
                @Override
                public void onResponse(Call<LiveStaffAttenResponse> call, Response<LiveStaffAttenResponse> response) {
                    Log.d(TAG ,response.body().getError()+" response "+response.body().getLiveAttendanceClass().size());
                    progressDialog.dismiss();
                    List<LiveAttendanceClas> studentAttenList = new ArrayList<>();
                    if(response.body().getError() == false){
                        studentAttenList = response.body().getLiveAttendanceClass();
                        Log.d(TAG ," response "+response.body().getLiveAttendanceClass().size());
                        if(studentAttenList.size() > 0)
                        {
                            liveattenHead.setVisibility(View.VISIBLE);
                            noattendance.setVisibility(View.GONE);
                            LiveClassAttenadapter liveStudAttenadapter = new LiveClassAttenadapter(AttendanceStaff1.this,studentAttenList);
                            rv_liveattendance.setAdapter(liveStudAttenadapter);
                        }else if(studentAttenList.size() == 0){
                            liveattenHead.setVisibility(View.GONE);
                            noattendance.setVisibility(View.VISIBLE);
                        }
                    }else {
                        liveattenHead.setVisibility(View.GONE);
                        noattendance.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onFailure(Call<LiveStaffAttenResponse> call, Throwable t) {
                    liveattenHead.setVisibility(View.GONE);
                    noattendance.setVisibility(View.VISIBLE);
                }
            });
        }else {
            showsnackbar();
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
                        if (InternetCheck.isInternetOn(AttendanceStaff1.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
