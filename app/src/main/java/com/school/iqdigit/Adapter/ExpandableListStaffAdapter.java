package com.school.iqdigit.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.iqdigit.Activity.StudyMaterialStaff1Activity;
import com.school.iqdigit.Activity.StudyMaterialStaffTutActivity;
import com.school.iqdigit.Activity.StudyMaterialTutorialActivity;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpandableListStaffAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private String TAG = "ExpandableListStaffAdapter";
    private String subject_id;

    public ExpandableListStaffAdapter(Context context, List<String> listDataHeader,
                                      HashMap<String, List<String>> listChildData, String subject_id) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.subject_id = subject_id;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        Log.d(TAG , childText+ " childText");
        StringTokenizer tokens = new StringTokenizer(childText, "^");
        String chaptername = tokens.nextToken();
        String chapterid = tokens.nextToken();
        Log.d(TAG, "First_string : " + chaptername);
        Log.d(TAG, "File_Ext : " + chapterid);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_staff_items, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(chaptername);

        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, StudyMaterialStaffTutActivity.class);
                intent.putExtra("chapterid", chapterid);
                intent.putExtra("chaptername", chaptername);
                intent.putExtra("subject_id", subject_id);
                intent.putExtra("type","");
                _context.startActivity(intent);
            }
        });
        ImageView imgeditchapter = convertView.findViewById(R.id.imgeditchapter);
        imgeditchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Showeditchapterpopup("Edit Chapter Name", "Please Enter Chapter name here", chapterid, chaptername);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        Log.d(TAG, headerTitle + " header");
        StringTokenizer tokens = new StringTokenizer(headerTitle, "^");
        String unitname = tokens.nextToken();
        String unitid1 = tokens.nextToken();
        Log.d(TAG, "unitname : " + unitname);
        Log.d(TAG, "unitid : " + unitid1);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_list_staff_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(unitname);
        ImageView imgeditunit = (ImageView) convertView.findViewById(R.id.imgeditunit);
        imgeditunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Showpopup("Edit Unit", "Please Enter Unit Name", unitid1, unitname);
            }
        });
        ImageView imgaddchapter = convertView.findViewById(R.id.imgaddchapter);
        imgaddchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Showpopup("Add Chapter", "Please Enter Chapter Name", unitid1, unitname);
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void Showpopup(String action, String edittexthint, String unitid, String unitname) {
        Dialog dialog;
        FloatingActionButton imgcancel;
        Button btnOk;
        EditText enterunit;
        TextView tv_title;
        dialog = new Dialog(_context);
        dialog.setContentView(R.layout.dialog_addunit);
        imgcancel = dialog.findViewById(R.id.bt_close);
        tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setText(action);
        btnOk = dialog.findViewById(R.id.btnok);
        enterunit = dialog.findViewById(R.id.ed_addunit);
        enterunit.setHint(edittexthint);
        if (action.equals("Edit Unit")) {
            enterunit.setText(unitname);
        }
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equals("Edit Unit")) {
                    if(!enterunit.getText().toString().equals("") && !unitid.equals("")) {
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().edit_unit(unitid, enterunit.getText().toString());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isErr() == false) {
                                    Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    ((Activity) _context).finish();
                                    _context.startActivity(((Activity) _context).getIntent());
                                } else {
                                    Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });
                    }
                    else {
                        Toast.makeText(_context,"Please Enter Unit Name before submit",Toast.LENGTH_LONG).show();
                    }
                } else if (action.equals("Add Chapter")) {
                    if(!enterunit.getText().toString().equals("") && !unitid.equals("")) {
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().add_chapter(unitid, enterunit.getText().toString());
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isErr() == false) {
                                        Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        ((Activity) _context).finish();
                                        _context.startActivity(((Activity) _context).getIntent());
                                    } else {
                                        Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            }
                        });
                    }else {
                        Toast.makeText(_context,"Please Enter Chapter Name before submit",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
    }

    public void Showeditchapterpopup(String action, String edittexthint, String chapterid, String chaptername) {
        Dialog dialog;
        FloatingActionButton imgcancel;
        Button btnOk;
        EditText enterunit;
        TextView tv_title;
        dialog = new Dialog(_context);
        dialog.setContentView(R.layout.dialog_addunit);
        imgcancel = dialog.findViewById(R.id.bt_close);
        tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setText(action);
        btnOk = dialog.findViewById(R.id.btnok);
        enterunit = dialog.findViewById(R.id.ed_addunit);
        enterunit.setHint(edittexthint);
        enterunit.setText(chaptername);

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enterunit.getText().toString().equals("") && !chapterid.equals("")) {
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().edit_chapter(chapterid, enterunit.getText().toString());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isErr() == false) {
                                    Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    ((Activity) _context).finish();
                                    _context.startActivity(((Activity) _context).getIntent());
                                } else {
                                    Toast.makeText(_context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });
                }else {
                    Toast.makeText(_context,"Please Enter Chapter Name before submit",Toast.LENGTH_LONG).show();
                }
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
    }
}