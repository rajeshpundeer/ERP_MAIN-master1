package com.school.iqdigit.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.FeelistdataResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SchooldataResponse;
import com.school.iqdigit.Modeldata.Feelistdata;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class feereceipt extends AppCompatActivity {
    private TextView name, dob, classn , addmno, receiptno, date, paymentmode, totalamount, ref, s_name, s_add;
    private ImageView logo;
    private RecyclerView recyclerView;
    private Intent mainintent;
    private List<Feelistdata> feelistdataList;
    private Button save_receipt;
    private boolean checkinternet;
    private ProgressDialog mProg;
    private String TAG = "feereceipt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feereceipt);
        mainintent = getIntent();
        recyclerView= findViewById(R.id.recycleareceipt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        name = findViewById(R.id.r_student_name);
        dob = findViewById(R.id.r_student_dob);
        classn = findViewById(R.id.r_student_class);
        addmno = findViewById(R.id.r_student_admno);
        receiptno = findViewById(R.id.r_receipt_bo);
        date = findViewById(R.id.r_receipt_date);
        ref = findViewById(R.id.r_ref_details);
        totalamount = findViewById(R.id.r_totalamm);
        paymentmode = findViewById(R.id.r_payment_mode);
        save_receipt = findViewById(R.id.print);
        logo = findViewById(R.id.school_logo);
        s_name = findViewById(R.id.school_name);
        s_add = findViewById(R.id.school_add);
        mProg = new ProgressDialog(this);
        receiptno.setText(":   "+mainintent.getStringExtra("re_no"));
        date.setText(":   "+mainintent.getStringExtra("re_date"));
        paymentmode.setText(mainintent.getStringExtra("re_mode"));
        ref.setText(mainintent.getStringExtra("re_ref"));
        checkinternet = InternetCheck.isInternetOn(feereceipt.this);
        save_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  save_receipt.setVisibility(View.GONE);
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
            }
        });
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        //Get School Data
        Call<SchooldataResponse> call = RetrofitClient.getInstance().getApi().getSchoolDetails();
        call.enqueue(new Callback<SchooldataResponse>() {
            @Override
            public void onResponse(Call<SchooldataResponse> call, Response<SchooldataResponse> response) {
                if (InternetCheck.isInternetOn(feereceipt.this) == true) {
                    mProg.dismiss();
                SchooldataResponse schooldataResponse = response.body();
                final String imgeurl = BASE_URL2 +"office_admin/maintenance_image/school_logo/"+ schooldataResponse.getSchool().getLogo();
               Log.d(TAG , imgeurl +" imglogo");
                //Glide.with(feereceipt.this).load(imgeurl).into(logo);
                    logo.setImageResource(R.mipmap.ic_launcher);
                s_name.setText(schooldataResponse.getSchool().getSch_cname());
                s_add.setText(schooldataResponse.getSchool().getSch_add());}
                else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<SchooldataResponse> call, Throwable t) {
                mProg.dismiss();
                if (InternetCheck.isInternetOn(feereceipt.this) != true) {
                    showsnackbar();
                }
            }
        });



        // Get Data of User to show at Header of This page.
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<ProfileResponse> call2 = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call2.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (InternetCheck.isInternetOn(feereceipt.this) == true) {
                    ProfileResponse profileResponse = response.body();
                    name.setText(":   " + profileResponse.getUserprofile().getName() + " " + profileResponse.getUserprofile().getStud_lname());
                    classn.setText(":   " + profileResponse.getUserprofile().getClass_r());
                    addmno.setText(":   " + profileResponse.getUserprofile().getAdm_no());
                    dob.setText(":   " + profileResponse.getUserprofile().getDob());
                }else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (InternetCheck.isInternetOn(feereceipt.this) != true) {
                    showsnackbar();
                }
               // Toast.makeText(feereceipt.this, "Error :"+ t , Toast.LENGTH_SHORT).show();
            }
        });

        Call<FeelistdataResponse> call1 = RetrofitClient.getInstance().getApi().getAReceipt(mainintent.getStringExtra("re_fid"));
        call1.enqueue(new Callback<FeelistdataResponse>() {
            @Override
            public void onResponse(Call<FeelistdataResponse> call, Response<FeelistdataResponse> response) {
                if (InternetCheck.isInternetOn(feereceipt.this) == true) {
                    feelistdataList = response.body().getReceipt();
                    receiptadapter adapter = new receiptadapter(feereceipt.this, feelistdataList);
                    recyclerView.setAdapter(adapter);
                }else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<FeelistdataResponse> call, Throwable t) {
                if (InternetCheck.isInternetOn(feereceipt.this) != true) {
                    showsnackbar();
                }
            }
        });
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        //save_receipt.setVisibility(View.VISIBLE);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        String m = String.valueOf(n);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/RECEIPT"+m+".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
           // Toast.makeText(this, "Payment Receipt "+"/RECEIPT"+m+".png" +" has been saved in your gallery.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }




    private class receiptadapter extends RecyclerView.Adapter<receiptadapter.receiptholder>{
        private Context mctx;
        private List<Feelistdata> feelistdataList;

        public receiptadapter(Context mctx, List<Feelistdata> feelistdataList) {
            this.mctx = mctx;
            this.feelistdataList = feelistdataList;
        }

        @NonNull
        @Override
        public receiptholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mctx).inflate(R.layout.recycle_a_receipt_of_fee,viewGroup,false);
            return new receiptholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull receiptholder receiptholder, int i) {
            Feelistdata feelistdata = feelistdataList.get(i);
            receiptholder.r_type.setText(feelistdata.getFeetype());
            receiptholder.r_amount.setText(feelistdata.getPaidamount());
            int p_type = Integer.parseInt(feelistdata.getDtype());
            if(p_type == 12){
                receiptholder.r_Duration.setText("Annual[ "+feelistdata.getDuration()+" ]");
            }else{
                receiptholder.r_Duration.setText("Recurring[ "+feelistdata.getDuration()+" ]");
            }
            totalamount.setText(feelistdata.getTotalamount());


        }

        @Override
        public int getItemCount() {
            return feelistdataList.size();
        }

        class receiptholder extends RecyclerView.ViewHolder{
            TextView r_type, r_Duration, r_amount;
            public receiptholder(@NonNull View itemView) {
                super(itemView);
                r_type = itemView.findViewById(R.id.r_type);
                r_Duration = itemView.findViewById(R.id.r_duration);
                r_amount = itemView.findViewById(R.id.r_amount);
            }
        }
    }

    private void showsnackbar(){
        if(mProg.isShowing()){
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( InternetCheck.isInternetOn(feereceipt.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
