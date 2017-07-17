package com.fundit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AreaItem;
import com.fundit.model.AreaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralMemberProfileActivity extends AppCompatActivity {

    EditText edt_firstName,edt_lastName,edt_assocOrganization,edt_assocFundspot,edt_contactInfo;
    ImageView img_profilePic;
    Button btn_updateProfile;
    Spinner spn_state, spn_city;
    ArrayList<String> stateNames=new ArrayList<>();
    ArrayList<AreaItem> stateItems=new ArrayList<>();
    ArrayList<String> cityNames=new ArrayList<>();
    ArrayList<AreaItem> cityItems=new ArrayList<>();
    ArrayAdapter<String> stateAdapter, cityAdapter;
    AdminAPI adminAPI;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_member_profile);

        dialog=new CustomDialog(this);
        adminAPI= ServiceGenerator.getAPIClass();

        fetchIDs();

    }

    private void fetchIDs() {
        edt_firstName=(EditText) findViewById(R.id.edt_firstName);
        edt_lastName=(EditText) findViewById(R.id.edt_lastName);
        edt_assocOrganization=(EditText) findViewById(R.id.edt_assocOrganization);
        edt_assocFundspot=(EditText) findViewById(R.id.edt_assocFundspot);
        edt_contactInfo=(EditText) findViewById(R.id.edt_contactInfo);

        img_profilePic=(ImageView) findViewById(R.id.img_profilePic);

        btn_updateProfile=(Button) findViewById(R.id.btn_updateProfile);

        spn_state=(Spinner) findViewById(R.id.spn_state);
        spn_city=(Spinner) findViewById(R.id.spn_city);

        stateAdapter=new ArrayAdapter<String>(this,R.layout.spinner_textview,stateNames);
        cityAdapter=new ArrayAdapter<String>(this,R.layout.spinner_textview,cityNames);

        spn_city.setAdapter(stateAdapter);
        spn_city.setAdapter(stateAdapter);

        dialog.show();
        Call<AreaResponse> stateCall=adminAPI.getStateList("1");
        stateCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                dialog.dismiss();
                stateNames.clear();
                stateItems.clear();

                AreaResponse areaResponse=response.body();
                if(areaResponse!=null){

                }
                else {

                }

            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}
