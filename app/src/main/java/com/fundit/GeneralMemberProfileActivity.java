package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;

public class GeneralMemberProfileActivity extends AppCompatActivity {

    EditText edt_firstName,edt_lastName,edt_assocOrganization,edt_assocFundspot,edt_contactInfo;
    ImageView img_profilePic;
    Button btn_updateProfile;
    Spinner spn_state, spn_city;
    AdminAPI adminAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_member_profile);

        adminAPI= ServiceGenerator.getAPIClass();

        fetchIDs();

    }

    private void fetchIDs() {

    }
}
