package com.school.iqdigit.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.FeeReceiptistResponse;
import com.school.iqdigit.Model.FeeStatusResponse;
import com.school.iqdigit.Model.GatewaydataResponse;
import com.school.iqdigit.Model.GetFeeOrderIdResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SubmitpaymentResponse;
import com.school.iqdigit.Model.feeamountresponse;
import com.school.iqdigit.Model.feelistresponse;
import com.school.iqdigit.Modeldata.Feeamountlist;
import com.school.iqdigit.Modeldata.Feelist;
import com.school.iqdigit.Modeldata.Feereceiptlist;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.AvenuesParams;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utility.ServiceUtility;
import com.school.iqdigit.utils.SharedHelper;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class feepayment extends AppCompatActivity {
    private LinearLayout feemlayout;
    private Spinner mothnames;
    private RecyclerView recyclerView, recyclerView2, recyclerView3;
    private List<Feeamountlist> feeamountlistList;
    private List<Feereceiptlist> feereceiptlistList;
    private TextView student_name, studenr_class, student_addmno, student_rollno, status, total_amount;
    private ImageView backbtn, userimage;
    private Spinner spinner;
    private LinearLayout linearLayout, feereceiptlistlayout;
    private String Total_payble_amount, Gatewayname;
    private int GATEWAY_NAME_CODE = 2;
    private Button paynowbtn, viewreceiptlistbtn;
    private Intent mainintent;
    private ProgressDialog mProg;
    private int i;
    private AppCompatButton paynow;
    private String _mothname, _monthid;
    private String[] FIDS;
    private String gFid;
    private Dialog myDialog2;
    private String default_selection= "";
    //Gateway Parameters
    private String TAG = "feepayment";
    private String merchant_id, access_code, api_key, redirect_url, return_url_reg, cancel_url_reg, orderId,orderIdtrack1;
    private boolean checkinternet;
    private String feeStatus;
    private String feeStatusMsg;
    private String fathername;
    private String mothername;
    private String rollno;
    private String admno;
    private String classr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feepayment);
        myDialog2 = new Dialog(this);
        mProg = new ProgressDialog(this);
        feemlayout = findViewById(R.id.feemothlayout);
        paynow = findViewById(R.id.paynowbtn);
        paynowbtn = findViewById(R.id.paynow);
        total_amount = findViewById(R.id.totalamm);
        mothnames = findViewById(R.id.monthspinner);
        backbtn = findViewById(R.id.backbtn);
        viewreceiptlistbtn = findViewById(R.id.view_receipt);
        student_name = findViewById(R.id.student_name);
        studenr_class = findViewById(R.id.student_class);
        student_addmno = findViewById(R.id.student_admno);
        student_rollno = findViewById(R.id.student_rollno);
        userimage = findViewById(R.id.user_images);
        status = findViewById(R.id.status);
        linearLayout = findViewById(R.id.paymentLayout);
        feereceiptlistlayout = findViewById(R.id.feereceiptlistlayour);
        recyclerView = findViewById(R.id.feemonthrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3 = findViewById(R.id.recyclefeereceiptlist);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2 = findViewById(R.id.recyclefeeamount);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mainintent = getIntent();
        checkinternet = InternetCheck.isInternetOn(feepayment.this);
        recyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Toast.makeText(this,"Today's Date: " + currentYear + currentMonth + currentDay, Toast.LENGTH_SHORT).show();
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name);
        mProg.show();

        final User user = SharedPrefManager.getInstance(feepayment.this).getUser();

        //get payment status from api
        if (InternetCheck.isInternetOn(feepayment.this) == true) {
            mProg.setMessage("Loading.....");
            mProg.setTitle(R.string.app_name);
            mProg.show();

            Call<FeeStatusResponse> call12 = RetrofitClient
                    .getInstance().getApi().getvalidatePayfeeAttempt(user.getId());
            call12.enqueue(new Callback<FeeStatusResponse>() {
                @Override
                public void onResponse(Call<FeeStatusResponse> call, Response<FeeStatusResponse> response) {
                    mProg.dismiss();
                    feeStatus = response.body().validate_payfee_attempt.can_proceed;
                    feeStatusMsg = response.body().validate_payfee_attempt.message;
                    Log.d(TAG , feeStatus+" "+feeStatusMsg);
                }
                @Override
                public void onFailure(Call<FeeStatusResponse> call, Throwable t) {
                    mProg.dismiss();
                }
            });
        }
        else {
            showsnackbar();
        }

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.isInternetOn(feepayment.this) == true) {
                    mProg.setMessage("Loading.....");
                    mProg.setTitle(R.string.app_name);
                    mProg.show();

                    Call<feeamountresponse> call12 = RetrofitClient
                            .getInstance().getApi().getFeeamountDeatils(user.getId(), _monthid);
                    call12.enqueue(new Callback<feeamountresponse>() {
                        @Override
                        public void onResponse(Call<feeamountresponse> call, Response<feeamountresponse> response) {
                            feeamountlistList = response.body().getFee_details();
                            if (!(feeamountlistList.size() == 0)) {
                                mProg.dismiss();
                                feemlayout.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                feereceiptlistlayout.setVisibility(View.GONE);
                                feelistmountadapter adapter2 = new feelistmountadapter(feepayment.this, feeamountlistList);
                                recyclerView2.setAdapter(adapter2);
                            } else {
                                mProg.dismiss();
                                Toast.makeText(feepayment.this, "Fee Already Paid " + "upto " + _monthid, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<feeamountresponse> call, Throwable t) {
                            mProg.dismiss();
                        }
                    });
                }
                else {
                    showsnackbar();
                }
            }
        });


        if (mainintent.getStringExtra("type").equals("feestructure")) {
//             monthid = new String[1][1];
//            Call<feelistresponse> call3  = RetrofitClient
//                    .getInstance().getApi().getFeeMonth();
//            call3.enqueue(new Callback<feelistresponse>() {
//                @Override
//                public void onResponse(Call<feelistresponse> call, Response<feelistresponse> response) {
//
//                    List<Feelist> feelistList;
//                    feelistresponse feelistresponse = response.body();
//                    feelistList = feelistresponse.getUser_fee_struc();
//
//                    //Creating an String array for the ListView
//                    monthnname = new String[feelistList.size()];
//                    monthid[0] = new String[feelistList.size()];
//
//                    //looping through all the heroes and inserting the names inside the string array
//                    for (int i = 0; i < feelistList.size(); i++) {
//                        monthnname[i] = feelistList.get(i).getMonth();
//                        monthid[0][i] = feelistList.get(i).getMonthnum();
//                    }
//
//                    ArrayAdapter<String> adapter2;
//                    adapter2 = new ArrayAdapter<String>(feepayment.this, android.R.layout.simple_list_item_1, monthnname);
//                    //setting adapter to spinner
//                    mothnames.setAdapter(adapter2);
//
//                    _monthid =monthid[0][0];
//
////                    feelistList = response.body().getUser_fee_struc();
////                    feelistadapter adapter = new feelistadapter(feepayment.this,feelistList);
////                    recyclerView.setAdapter(adapter);
//                    mProg.dismiss();
//                }
//
//                @Override
//                public void onFailure(Call<feelistresponse> call, Throwable t) {
//                    Toast.makeText(feepayment.this, ""+t, Toast.LENGTH_SHORT).show();
//                    mProg.dismiss();
//                }
//            });
//
//            mothnames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//            {
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//                {
//                    Toast.makeText(feepayment.this, ""+_monthid, Toast.LENGTH_SHORT).show();
//                    String mid = monthid[0][position];
//                    _monthid = mid;
//                }
//                public void onNothingSelected(AdapterView<?> parent)
//                {
//
//                }
//            });
            if (InternetCheck.isInternetOn(feepayment.this) == true) {
                getmoths();}
            else {
                showsnackbar();
            }
        } else {
            Call<FeeReceiptistResponse> call = RetrofitClient.getInstance().getApi().getAllreceipt(user.getId());
            call.enqueue(new Callback<FeeReceiptistResponse>() {
                @Override
                public void onResponse(Call<FeeReceiptistResponse> call, Response<FeeReceiptistResponse> response) {
                    if (InternetCheck.isInternetOn(feepayment.this) == true) {
                        if(response.isSuccessful()) {
                            feereceiptlistList = response.body().getAllreceipt();
                            feereceiptlistadapter adapter = new feereceiptlistadapter(feepayment.this, feereceiptlistList);
                            recyclerView3.setAdapter(adapter);
                        }
                        mProg.dismiss();
                    }
                    else {
                        showsnackbar();
                    }
                }

                @Override
                public void onFailure(Call<FeeReceiptistResponse> call, Throwable t) {
                    mProg.dismiss();
                }
            });
            feemlayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            feereceiptlistlayout.setVisibility(View.VISIBLE);
        }


        viewreceiptlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//Check Gateway Profile

        Call<GatewaydataResponse> call = RetrofitClient
                .getInstance().getApi().getGatewaydata(user.getId());
        call.enqueue(new Callback<GatewaydataResponse>() {
            @Override
            public void onResponse(Call<GatewaydataResponse> call, Response<GatewaydataResponse> response) {

                if(response.isSuccessful()) {
                    GatewaydataResponse gatewaydataResponse = response.body();
                    Gatewayname = gatewaydataResponse.getGateway_deatils().getGateway_provider();
                    access_code = gatewaydataResponse.getGateway_deatils().getAccess_code();
                    merchant_id = gatewaydataResponse.getGateway_deatils().getMerchant_id();
                    redirect_url = gatewaydataResponse.getGateway_deatils().getRedirect_url();
                    return_url_reg = gatewaydataResponse.getGateway_deatils().getReturn_url_reg();
                    cancel_url_reg = gatewaydataResponse.getGateway_deatils().getCancel_url_reg();
                    api_key = gatewaydataResponse.getGateway_deatils().getApi_key();
                    //  student_addmno.setText(Gatewayname+" "+access_code+" "+merchant_id+" "+ redirect_url+" "+return_url_reg+ " "+ cancel_url_reg+" ");

                    if (Gatewayname.equals("traknpay")) {
                        GATEWAY_NAME_CODE = 0;
                    } else if (Gatewayname.equals("hdfc")) {
                        GATEWAY_NAME_CODE = 1;
                    } else {
                        paynowbtn.setVisibility(View.GONE);
                        Toast.makeText(feepayment.this, "Online feestructure payment not available", Toast.LENGTH_SHORT).show();
                        GATEWAY_NAME_CODE = 2;
                    }
                    Log.d("GATEWAY_NAME_CODE",Integer.toString(GATEWAY_NAME_CODE));
                }else
                {
                    Toast.makeText(feepayment.this, "Online feestructure payment not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GatewaydataResponse> call, Throwable t) {

            }
        });

        paynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(feepayment.this);
                dialog.setMessage(feeStatusMsg);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dialog.setMessage(Html.fromHtml(feeStatusMsg, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    dialog.setMessage(Html.fromHtml(feeStatusMsg));
                }
                dialog.setTitle("Important Note");
                if(feeStatus.equalsIgnoreCase("Yes")) {
                    dialog.setPositiveButton("Pay Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //Fee Payment connect to payment gateway
                                    if (GATEWAY_NAME_CODE == 0) {
                                        if (InternetCheck.isInternetOn(feepayment.this) == true) {
                                            getorderid1();
                                            //paybytraknpay();
                                        }
                                        else {
                                            showsnackbar();
                                        }
                                    } else if (GATEWAY_NAME_CODE == 1) {
                                        if (InternetCheck.isInternetOn(feepayment.this) == true) {
                                            payByCCavenue();
                                        }else {
                                            showsnackbar();
                                        }
                                    } else if (GATEWAY_NAME_CODE == 2) {

                                        Toast.makeText(feepayment.this, "Online Fee Payment not available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(feepayment.this, MainActivity.class));
            }
        });




        // Get Data of User to show at Header of This page.
        Call<ProfileResponse> call2 = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call2.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (InternetCheck.isInternetOn(feepayment.this) == true) {
                    ProfileResponse profileResponse = response.body();
                    student_name.setText(profileResponse.getUserprofile().getName() + " " + profileResponse.getUserprofile().getStud_lname());
                    studenr_class.setText("Class: " + profileResponse.getUserprofile().getClass_r());
                    student_addmno.setText("Adm. No.: " + profileResponse.getUserprofile().getAdm_no());
                    student_rollno.setText("Roll No.: " + profileResponse.getUserprofile().getRoll_no());
                    admno = profileResponse.getUserprofile().getAdm_no();
                    rollno = profileResponse.getUserprofile().getRoll_no();
                    classr = profileResponse.getUserprofile().getClass_r();
                    fathername = profileResponse.getUserprofile().getFather_name();
                    mothername = profileResponse.getUserprofile().getMother_name();
                    status.setText(profileResponse.getUserprofile().getStud_status());
                    final String imgeurl = BASE_URL2 + "office_admin/maintenance_image/student_photos/" + profileResponse.getUserprofile().getPhoto();
                    Glide.with(feepayment.this).load(imgeurl)
                            .placeholder(ContextCompat.getDrawable(feepayment.this, R.drawable.user_icon))
                            .into(userimage);
                }
                else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (InternetCheck.isInternetOn(feepayment.this) != true) {
                    Log.v(TAG, "Error1 :" + t);
                    // Toast.makeText(feepayment.this, "Error :" + t, Toast.LENGTH_SHORT).show();
                    showsnackbar();
                }
            }
        });


        // Get List of Month to pay fees.


    }

    private void getmoths() {
        User user = SharedPrefManager.getInstance(this).getUser();
        Log.d(TAG, "user " + user.getId());
        final String[][] monthid = new String[1][1];
        Call<feelistresponse> call3 = RetrofitClient
                .getInstance().getApi().getFeeMonth(user.getId());
        call3.enqueue(new Callback<feelistresponse>() {
            @Override
            public void onResponse(Call<feelistresponse> call, Response<feelistresponse> response) {
                Log.d(TAG,"response.raw().request().url();"+response.raw().request().url());

                if(response.isSuccessful()) {
                    List<Feelist> feelistList = new ArrayList<>();
                    feelistresponse feelistresponse = response.body();
                    feelistList = feelistresponse.getUser_fee_struc();

                    default_selection = feelistresponse.getDefault_selection();
                    //Creating an String array for the ListView
                    String[] monthnname = new String[feelistList.size()];
                    monthid[0] = new String[feelistList.size()];

                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < feelistList.size(); i++) {
                        monthnname[i] = feelistList.get(i).getMonth();
                        monthid[0][i] = feelistList.get(i).getMonthnum();
                        Log.v("months", monthid[0][i].toString());
                    }

                    ArrayAdapter<String> adapter2;
                    adapter2 = new ArrayAdapter<String>(feepayment.this, android.R.layout.simple_list_item_1, monthnname);
                    //setting adapter to spinner
                    mothnames.setAdapter(adapter2);
                    _monthid = monthid[0][0];
                    for (int i = 0; i < feelistList.size(); i++)
                    {
                        Log.d(TAG , feelistList.get(i).getMonthnum()+" month name "+ default_selection+ " default_selection");
                        if (feelistList.get(i).getMonthnum().equals(default_selection))
                        {
                            mothnames.setSelection(i);
                            Log.d(TAG , monthnname+" month name " +monthid[0][i]+ " month id");
                            _monthid = monthid[0][i];
                        }
                    }
//                    feelistList = response.body().getUser_fee_struc();
//                    feelistadapter adapter = new feelistadapter(feepayment.this,feelistList);
//                    recyclerView.setAdapter(adapter);
                }
                mProg.dismiss();
            }

            @Override
            public void onFailure(Call<feelistresponse> call, Throwable t) {
                Toast.makeText(feepayment.this, "" + t, Toast.LENGTH_SHORT).show();
                mProg.dismiss();
            }
        });

        mothnames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(monthid[0][position] != null) {
                    String mid = monthid[0][position];
                    _monthid = mid;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    // Recycle Adapter to get Fee Receipt list.
    private class feereceiptlistadapter extends RecyclerView.Adapter<feereceiptlistadapter.feereceiptlistholder> {
        private Context mCtx;
        private List<Feereceiptlist> feereceiptlistList;

        public feereceiptlistadapter(Context mCtx, List<Feereceiptlist> feereceiptlistList) {
            this.mCtx = mCtx;
            this.feereceiptlistList = feereceiptlistList;
        }

        @NonNull
        @Override
        public feereceiptlistholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_receipt_of_fee, viewGroup, false);
            return new feereceiptlistholder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull feereceiptlistholder feereceiptlistholder, int i) {
            final Feereceiptlist feereceiptlist = feereceiptlistList.get(i);
            String sno = Integer.toString(i + 1);
            feereceiptlistholder.re_date.setText(sno + "   " + feereceiptlist.getFee_paid_date());
            feereceiptlistholder.re_amount.setText("₹" + feereceiptlist.getPaid_amount());
            feereceiptlistholder.re_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, feereceipt.class);
                    intent.putExtra("re_no", feereceiptlist.getFee_receiptno());
                    intent.putExtra("re_date", feereceiptlist.getFee_paid_date());
                    intent.putExtra("re_mode", feereceiptlist.getPay_mode());
                    intent.putExtra("re_fid", feereceiptlist.getFid());
                    intent.putExtra("re_ref", feereceiptlist.getRef_num());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return feereceiptlistList.size();
        }

        class feereceiptlistholder extends RecyclerView.ViewHolder {
            TextView re_date, re_amount;
            Button re_view;

            public feereceiptlistholder(@NonNull View itemView) {
                super(itemView);
                re_date = itemView.findViewById(R.id.re_date);
                re_amount = itemView.findViewById(R.id.re_amount);
                re_view = itemView.findViewById(R.id.re_view);
            }
        }
    }

    // Recycle Adapter to get Fee Amount For selected Months.

    private class feelistmountadapter extends RecyclerView.Adapter<feelistmountadapter.feeamountholder>{
        private Context mmctx;
        private List<Feeamountlist> feeamountlistList;

        public feelistmountadapter(Context mmctx, List<Feeamountlist> feeamountlistList) {
            this.mmctx = mmctx;
            this.feeamountlistList = feeamountlistList;
        }

        @NonNull
        @Override
        public feeamountholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mmctx).inflate(R.layout.recycle_details_of_fee,viewGroup,false);

            return new feeamountholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull feeamountholder feeamountholder, int i) {
            Feeamountlist feeamountlist = feeamountlistList.get(i);
            feeamountholder.feetype.setText(feeamountlist.getFee_type());
            feeamountholder.amount.setText(feeamountlist.getFee_amount());
            feeamountholder.month.setText(feeamountlist.getFee_month());
            total_amount.setText(feeamountlist.getTotal_fee_amount());

            if(i == 0){
                gFid = feeamountlist.getFee_id();
            }
//            else if(i != feeamountlistList.size() - 1){
//
//            }
            else {
                gFid = gFid + "," + feeamountlist.getFee_id();
            }

        }

        @Override
        public int getItemCount() {

            return feeamountlistList.size();
        }

        class feeamountholder extends RecyclerView.ViewHolder{
            TextView no, feetype, month, amount ;
            public feeamountholder(@NonNull View itemView) {
                super(itemView);
                feetype = (TextView) itemView.findViewById(R.id.feetype);
                amount = (TextView) itemView.findViewById(R.id.feeamount);
                month = (TextView) itemView.findViewById(R.id.feemonth);
            }
        }

    }


    // Recycle Adapter to get Months to pay fees.

    private class feelistadapter extends RecyclerView.Adapter<feelistadapter.feelistholder>{
        private Context mCtx;
        private List<Feelist> feelistList;

        public feelistadapter(Context mCtx, List<Feelist> feelistList) {
            this.mCtx = mCtx;
            this.feelistList = feelistList;
        }

        @NonNull
        @Override
        public feelistholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_month_of_fee,viewGroup,false);
            return new feelistholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull feelistholder feelistholder, int i) {
            User user = SharedPrefManager.getInstance(mCtx).getUser();
            final Feelist feelist = feelistList.get(i);
            feelistholder.monthname.setText("Pay upto " + feelist.getMonth());
            feelistholder.monthname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProg.setMessage("Loading.....");
                    mProg.setTitle(R.string.app_name);
                    mProg.show();
                    String month = feelist.getMonthnum();
                    User user = SharedPrefManager.getInstance(feepayment.this).getUser();
                    Call<feeamountresponse> call = RetrofitClient
                            .getInstance().getApi().getFeeamountDeatils(user.getId(),month);
                    call.enqueue(new Callback<feeamountresponse>() {
                        @Override
                        public void onResponse(Call<feeamountresponse> call, Response<feeamountresponse> response) {
                            feeamountlistList = response.body().getFee_details();
                            if(!(feeamountlistList.size() == 0)){
                                mProg.dismiss();
                                feemlayout.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                feereceiptlistlayout.setVisibility(View.GONE);
                                feelistmountadapter adapter2 = new feelistmountadapter(feepayment.this,feeamountlistList);
                                recyclerView2.setAdapter(adapter2);
                            }else{
                                mProg.dismiss();
                                Toast.makeText(feepayment.this, "Fee Already Paid "+"upto " + feelist.getMonth(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<feeamountresponse> call, Throwable t) {
                            mProg.dismiss();
                        }
                    });
                }
            });

        }

        @Override
        public int getItemCount() {
            return feelistList.size();
        }

        class feelistholder extends RecyclerView.ViewHolder{
            TextView monthname;

            public feelistholder(@NonNull View itemView) {
                super(itemView);
                monthname = (TextView) itemView.findViewById(R.id.feemonthname);
            }
        }
    }
    private void payByCCavenue(){
        mProg.setCancelable(false);
        mProg.show();
        getorderid();
        //Generate Order ID
//        Random rnd = new Random();
//        int n = 100000 + rnd.nextInt(900000);
//        orderId = Integer.toString(n);
        //Mandatory parameters. Other parameters can be added if required.

    }

    private void getorderid() {
        User user =  SharedPrefManager.getInstance(this).getUser();
        Call<GetFeeOrderIdResponse> call = RetrofitClient.getInstance().getApi().addonlinepaymentorderid(user.getId(), String.valueOf(total_amount.getText()));
        call.enqueue(new Callback<GetFeeOrderIdResponse>() {
            @Override
            public void onResponse(Call<GetFeeOrderIdResponse> call, Response<GetFeeOrderIdResponse> response) {
                if (response.isSuccessful() && response != null) {
                    orderId = response.body().getOrderid();
                    SharedHelper.putKey(feepayment.this, "orderId",orderId );
                    mProg.dismiss();
                    if (GATEWAY_NAME_CODE != 0) {
                        requestccpay();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetFeeOrderIdResponse> call, Throwable t) {
                mProg.dismiss();
            }
        });
    }

    private void requestccpay() {
        User user =  SharedPrefManager.getInstance(this).getUser();
        String vAccessCode = ServiceUtility.chkNull(access_code).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchant_id).toString().trim();
        String vCurrency = ServiceUtility.chkNull("INR").toString().trim();
        String vAmount = ServiceUtility.chkNull(total_amount).toString().trim();
        if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(access_code).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchant_id).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull("INR").toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(total_amount.getText()).toString().trim());
            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(return_url_reg).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancel_url_reg).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(redirect_url).toString().trim());
            intent.putExtra(AvenuesParams.merchant_param1, user.getId().toString());
            intent.putExtra(AvenuesParams.merchant_param2,admno);
            intent.putExtra(AvenuesParams.merchant_param3, classr);
            intent.putExtra(AvenuesParams.merchant_param4, fathername);
            intent.putExtra(AvenuesParams.merchant_param5, mothername);
            intent.putExtra(AvenuesParams.FIDS, gFid);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Parameters Are Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getadmintionid(){
        final String[] adm = new String[1];
        if (InternetCheck.isInternetOn(feepayment.this) == true) {
            final User user = SharedPrefManager.getInstance(this).getUser();
            Call<ProfileResponse> call = RetrofitClient
                    .getInstance().getApi().getUserProfile(user.getId());
            call.enqueue(new Callback<ProfileResponse>() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    ProfileResponse profileResponse = response.body();
                    Log.d("ADM NUM", profileResponse.getUserprofile().getAdm_no());
                    adm[0] = profileResponse.getUserprofile().getAdm_no();
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                }
            });
        } else {
            showsnackbar();
        }
        return adm[0];

    }
    // TraknPay Gateway Call
    private void paybytraknpay() {
        String ADM = getadmintionid();

        SharedHelper.putKey(this, "amount" ,total_amount.getText().toString().trim());
        SharedHelper.putKey(this, "gFID",gFid );

        User user = SharedPrefManager.getInstance(feepayment.this).getUser();
        //API_KEY is given by the Payment Gateway. Please Copy Paste Here.
        final String PG_API_KEY = api_key;

        //URL to Accept Payment Response After Payment. This needs to be done at the client's web server.
        final String PG_RETURN_URL = "https://biz.traknpay.in/v2/paymentrequest";

        //Enter the Mode of Payment Here . Allowed Values are "LIVE" or "TEST".
        final String PG_MODE = "LIVE";

        //PG_CURRENCY is given by the Payment Gateway. Only "INR" Allowed.
        final String PG_CURRENCY = "INR";

        //PG_COUNTRY is given by the Payment Gateway. Only "IND" Allowed.
        final String PG_COUNTRY = "IND";
        final String PG_AMOUNT =   SharedHelper.getKey(this, "amount");
        final String PG_EMAIL = "info@iqwing.in";
        final String PG_NAME = user.getName()+" ID:"+user.getId()+" Adm ID:"+ADM;
        final String PG_PHONE = user.getPhone_number();
        String PG_ORDER_ID = "";
        final String PG_DESCRIPTION = "Paid By Traknpay";
        final String PG_CITY = "Hamirpur";
        final String PG_STATE = "Himachal Pradesh";
        final String PG_ADD_1 = "abc";
        final String PG_ADD_2 = "abc";
        final String PG_ZIPCODE = "307023";
        final String PG_UDF1 = user.getId().toString();
        final String PG_UDF2 =  admno;
        final String PG_UDF3 = classr;
        final String PG_UDF4 = fathername;
        final String PG_UDF5 = mothername;
        //  Random rnd = new Random();
        // int n = 100000 + rnd.nextInt(900000);
        PG_ORDER_ID = orderIdtrack1;

        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey(PG_API_KEY);
        pgPaymentParams.setAmount(PG_AMOUNT);
        pgPaymentParams.setEmail(PG_EMAIL);
        pgPaymentParams.setName(PG_NAME);
        pgPaymentParams.setPhone(PG_PHONE);
        pgPaymentParams.setOrderId(PG_ORDER_ID);
        pgPaymentParams.setCurrency(PG_CURRENCY);
        pgPaymentParams.setDescription(PG_DESCRIPTION);
        pgPaymentParams.setCity(PG_CITY);
        pgPaymentParams.setState(PG_STATE);
        pgPaymentParams.setAddressLine1(PG_ADD_1);
        pgPaymentParams.setAddressLine2(PG_ADD_2);
        pgPaymentParams.setZipCode(PG_ZIPCODE);
        pgPaymentParams.setCountry(PG_COUNTRY);
        pgPaymentParams.setReturnUrl(PG_RETURN_URL);
        pgPaymentParams.setMode(PG_MODE);
        pgPaymentParams.setUdf1(PG_UDF1);
        pgPaymentParams.setUdf2(PG_UDF2);
        pgPaymentParams.setUdf3(PG_UDF3);
        pgPaymentParams.setUdf4(PG_UDF4);
        pgPaymentParams.setUdf5(PG_UDF5);

        PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, feepayment.this);
        pgPaymentInitialzer.initiatePaymentProcess();

    }

    private void getorderid1() {
        User user =  SharedPrefManager.getInstance(this).getUser();
        Call<GetFeeOrderIdResponse> call = RetrofitClient.getInstance().getApi().addonlinepaymentorderid(user.getId(), String.valueOf(total_amount.getText()));
        call.enqueue(new Callback<GetFeeOrderIdResponse>() {
            @Override
            public void onResponse(Call<GetFeeOrderIdResponse> call, Response<GetFeeOrderIdResponse> response) {
                if (response.isSuccessful() && response != null) {
                    orderIdtrack1 = response.body().getOrderid();
                    Log.d("order_id",orderIdtrack1);
                    SharedHelper.putKey(feepayment.this, "orderIdtrack1",orderIdtrack1);
                    paybytraknpay();
                    mProg.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetFeeOrderIdResponse> call, Throwable t) {
                mProg.dismiss();
            }
        });
    }

    private void feepaymentdone() {
        mProg.setCancelable(false);
        mProg.show();
        final Dialog dialog = new Dialog(feepayment.this);
        String Message = "Error..\nGet Receipt Failed, Please Contact Administrator\nOrder ID: " +   SharedHelper.getKey(this,"orderId") + "\nAmount: " +    SharedHelper.getKey(this,"amount" );
        Log.v("feepaymentdone", Message);
        dialog.setTitle(Message);
        dialog.setCanceledOnTouchOutside(true);

        User user = SharedPrefManager.getInstance(this).getUser();
        Call<SubmitpaymentResponse> call = RetrofitClient.getInstance().getApi().submitpayment(
                user.getId(),
                SharedHelper.getKey(this,"gFID" ),
                SharedHelper.getKey(this,"amount" ),
                SharedHelper.getKey(this,"orderId"));

        Log.v("PARAM" , SharedHelper.getKey(this,"amount" )+"  "+
                SharedHelper.getKey(this,"gFID" )+"   "+
                SharedHelper.getKey(this,"orderId"));


        call.enqueue(new Callback<SubmitpaymentResponse>() {
            @Override
            public void onResponse(Call<SubmitpaymentResponse> call, Response<SubmitpaymentResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        ShowPopupDone();
                        mProg.dismiss();
                    } else {
                        mProg.dismiss();
                        Toast.makeText(feepayment.this, "Error Code 1", Toast.LENGTH_SHORT).show();
                        dialog.show();
                    }
                } else {
                    mProg.dismiss();
                    Toast.makeText(feepayment.this, "Error Code 2", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
                mProg.dismiss();
            }

            @Override
            public void onFailure(Call<SubmitpaymentResponse> call, Throwable t) {
                mProg.dismiss();
                Toast.makeText(feepayment.this, "Error Code 3", Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });
    }
    public void ShowPopupDone() {
        myDialog2.setContentView(R.layout.transactionsuccess);
        Button yes = myDialog2.findViewById(R.id.yes_logout);
        Button no = myDialog2.findViewById(R.id.no_logout);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(feepayment.this, feepayment.class);
                intent.putExtra("type", "Receipt");
                startActivity(intent);
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(feepayment.this, "Transaction Successful!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.closeOptionsMenu();
        myDialog2.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: " + paymentResponse);
                    if (paymentResponse.equals("null")) {
                        Toast.makeText(this, "Transaction Error", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
                        Log.v("Track_Response",response.toString() );
                        if (response.optString("response_code").equals("0")) {
                            feepaymentdone();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(feepayment.this, MainActivity.class));
    }

    private void showsnackbar(){
        if( mProg.isShowing())
        {
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( InternetCheck.isInternetOn(feepayment.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    protected void onRestart() {
        final User user = SharedPrefManager.getInstance(feepayment.this).getUser();
        //get payment status from api it is done or not
        if (InternetCheck.isInternetOn(feepayment.this) == true) {
            mProg.setMessage("Loading.....");
            mProg.setTitle(R.string.app_name);
            mProg.show();

            Call<FeeStatusResponse> call12 = RetrofitClient
                    .getInstance().getApi().getvalidatePayfeeAttempt(user.getId());
            call12.enqueue(new Callback<FeeStatusResponse>() {
                @Override
                public void onResponse(Call<FeeStatusResponse> call, Response<FeeStatusResponse> response) {
                    mProg.dismiss();
                    feeStatus = response.body().validate_payfee_attempt.can_proceed;
                    feeStatusMsg = response.body().validate_payfee_attempt.message;
                    Log.d(TAG , feeStatus+" "+feeStatusMsg);
                }
                @Override
                public void onFailure(Call<FeeStatusResponse> call, Throwable t) {
                    mProg.dismiss();
                }
            });
        }
        else {
            showsnackbar();
        }
        super.onRestart();
    }
}