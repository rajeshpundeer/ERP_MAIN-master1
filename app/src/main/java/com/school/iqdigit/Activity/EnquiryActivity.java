package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnquiryActivity extends AppCompatActivity {
    private NiceSpinner spCat;
    private ImageView backbtn;
    private ProgressDialog mProg;
    private Button btnSubmitEnq;
    private EditText edName, edMobileNumber, edEmail, edEnquiry;
    private ArrayList<String> category = new ArrayList<>();
    private String item = "Admission Enquiry";
    private String itemclass = "NA";
    private String TAG = "EnquiryActivity";
    private ArrayList<String> class_add = new ArrayList<>();
    private NiceSpinner edAdmissionSought;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        backbtn = findViewById(R.id.backbtnenq);
        btnSubmitEnq = findViewById(R.id.btnSubmitEnq);
        spCat = findViewById(R.id.spCat);
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edEnquiry = findViewById(R.id.edEnquiry);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        edAdmissionSought = findViewById(R.id.edAdmissionSought);
        //add items to catogery spinner
        category.add("Admission Enquiry");
        category.add("Job Enquiry");
        category.add("General Enquiry");
        category.add("Suggestion");
        category.add("Complaint");
        category.add("Others");
        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<String>(EnquiryActivity.this, android.R.layout.simple_list_item_1, category);
        //setting adapter to spinner
        spCat.setAdapter(adapter2);

        class_add();
        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<String>(EnquiryActivity.this, android.R.layout.simple_list_item_1, class_add);
        //setting adapter to spinner
        edAdmissionSought.setAdapter(adapter1);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubmitEnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edName.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!edMobileNumber.getText().toString().trim().equalsIgnoreCase("")) {
                        if (edMobileNumber.getText().toString().trim().length() >= 10) {
                            if (!edEmail.getText().toString().trim().equalsIgnoreCase("")) {
                                if (EMAIL_ADDRESS_PATTERN.matcher(edEmail.getText().toString()).matches()) {
                                    if (!edEnquiry.getText().toString().trim().equalsIgnoreCase("")) {
                                        mProg.show();
                                        if (InternetCheck.isInternetOn(EnquiryActivity.this) == true) {
                                            Log.d(TAG, " " + edName.getText().toString() + " " + edMobileNumber.getText().toString() + edEmail.getText().toString() + item + edEnquiry.getText().toString());
                                            Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().addenquiry(edName.getText().toString(), edMobileNumber.getText().toString(), edEmail.getText().toString(), item, edEnquiry.getText().toString(), itemclass);
                                            call.enqueue(new Callback<ErrorResponse>() {
                                                @Override
                                                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                                                    if (!response.body().isError()) {
                                                        mProg.dismiss();
                                                        edName.setText("");
                                                        edMobileNumber.setText("");
                                                        edEmail.setText("");
                                                        edEnquiry.setText("");

                                                        Toast.makeText(EnquiryActivity.this, " Enquiry Added Successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        mProg.dismiss();
                                                        Toast.makeText(EnquiryActivity.this, "Enquiry Added Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                                                    mProg.dismiss();
                                                }
                                            });
                                        } else {
                                            showsnackbar();
                                        }

                                    } else {
                                        edEnquiry.setError("Please enter your Enquiry description");
                                    }
                                } else {
                                    edEmail.setError("Please enter valid email");
                                }
                            } else {
                                edEmail.setError("Please enter your email");
                            }
                        } else {
                            edMobileNumber.setError("Please valid Mobile number");
                        }
                    } else {
                        edMobileNumber.setError("Please enter your Mobile number");
                    }
                } else {
                    edName.setError("Please enter your name");
                }
            }
        });


        spCat.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                item = spCat.getSelectedItem().toString();
                Log.d(TAG, item);
            }
        });
        edAdmissionSought.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                itemclass = edAdmissionSought.getSelectedItem().toString();
                Log.d(TAG, itemclass);
            }
        });
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private void class_add() {
        class_add.add("NA");
        class_add.add("Nursery");
        class_add.add("LKG");
        class_add.add("UKG");
        class_add.add("I");
        class_add.add("II");
        class_add.add("III");
        class_add.add("IV");
        class_add.add("V");
        class_add.add("VI");
        class_add.add("VII");
        class_add.add("VIII");
        class_add.add("IX");
        class_add.add("X");
        class_add.add("XI");
        class_add.add("XII");
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
                        if (InternetCheck.isInternetOn(EnquiryActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
