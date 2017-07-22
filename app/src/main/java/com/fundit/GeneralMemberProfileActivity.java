package com.fundit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.helper.FilePath;
import com.fundit.model.AreaItem;
import com.fundit.model.AreaResponse;
import com.fundit.model.Organization;
import com.fundit.model.OrganizationResponse;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralMemberProfileActivity extends AppCompatActivity {

    EditText edt_firstName, edt_lastName, edt_contactInfo, ed_member_address, ed_zip_code;
    ImageView img_uplode_photo, img_remove;
    Button btn_updateProfile;
    Spinner spn_state, spn_city, spn_assocOrganization, spn_assocFundspot;
    ArrayList<String> stateNames=new ArrayList<>();
    ArrayList<AreaItem> stateItems=new ArrayList<>();
    ArrayList<String> cityNames=new ArrayList<>();
    ArrayList<AreaItem> cityItems=new ArrayList<>();
    List<Organization> organizationList = new ArrayList<>();
    ArrayList<String> organizationNames = new ArrayList<>();
    List<Organization> fundSpotList = new ArrayList<>();
    ArrayList<String> fundspotNames = new ArrayList<>();
    ArrayAdapter<String> stateAdapter, cityAdapter, organizationAdapter, fundspotAdapter;
    AdminAPI adminAPI;
    CustomDialog dialog;

    boolean firstTime = false;

    String imagePath = null;

    int IMAGE_REQUEST = 145;

    AppPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_member_profile);
        Intent intent = getIntent();
        firstTime = intent.getBooleanExtra("firstTime", false);
        dialog=new CustomDialog(this);
        adminAPI= ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);

        setupToolbar();
        fetchIDs();

        Log.e("ID", preference.getUserID() + "--" + preference.getTokenHash());

    }

    private void fetchIDs() {
        edt_firstName = (EditText) findViewById(R.id.edt_firstName);
        edt_lastName = (EditText) findViewById(R.id.edt_lastName);
        spn_assocOrganization = (Spinner) findViewById(R.id.spn_assocOrganization);
        spn_assocFundspot = (Spinner) findViewById(R.id.spn_assocFundspot);
        edt_contactInfo = (EditText) findViewById(R.id.edt_contactInfo);
        ed_member_address=(EditText)findViewById(R.id.ed_member_address);
        ed_zip_code=(EditText)findViewById(R.id.ed_zip_code);


        img_uplode_photo = (ImageView) findViewById(R.id.img_profilePic);
        img_remove = (ImageView) findViewById(R.id.img_remove);

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.img)
                        .into(img_uplode_photo);
                imagePath = null;
                img_remove.setVisibility(View.GONE);
            }
        });

        btn_updateProfile = (Button) findViewById(R.id.btn_updateProfile);

        spn_state = (Spinner) findViewById(R.id.spn_state);
        spn_city = (Spinner) findViewById(R.id.spn_city);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, cityNames);
        organizationAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, organizationNames);
        fundspotAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, fundspotNames);

        spn_state.setAdapter(stateAdapter);
        spn_city.setAdapter(cityAdapter);
        spn_assocOrganization.setAdapter(organizationAdapter);
        spn_assocFundspot.setAdapter(fundspotAdapter);

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    clearCities();
                } else {
                    loadCities(stateItems.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
        Call<AreaResponse> stateCall = adminAPI.getStateList("1");
        stateCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                dialog.dismiss();
                clearStates();

                AreaResponse areaResponse = response.body();
                if (areaResponse != null) {
                    if (areaResponse.isStatus()) {
                        stateItems.addAll(areaResponse.getData());
                        stateNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                }

                stateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();
                clearStates();
            }
        });

        Call<OrganizationResponse> organizationResponseCall=adminAPI.getAllOrganizations();
        organizationResponseCall.enqueue(new Callback<OrganizationResponse>() {
            @Override
            public void onResponse(Call<OrganizationResponse> call, Response<OrganizationResponse> response) {
                clearAssociatedOrganization();
                OrganizationResponse orgResponse=response.body();
                if(orgResponse!=null){
                    if(orgResponse.isStatus()){
                        organizationList.addAll(orgResponse.getData());
                        organizationNames.addAll(orgResponse.getOrganizationNames());
                    }
                }
                organizationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrganizationResponse> call, Throwable t) {
                clearAssociatedOrganization();
            }
        });

        Call<OrganizationResponse> fundspotResponseCall=adminAPI.getAllFundspots();
        fundspotResponseCall.enqueue(new Callback<OrganizationResponse>() {
            @Override
            public void onResponse(Call<OrganizationResponse> call, Response<OrganizationResponse> response) {
                clearAssociatedFundspot();
                OrganizationResponse orgResponse=response.body();
                if(orgResponse!=null){
                    if(orgResponse.isStatus()){
                        fundSpotList.addAll(orgResponse.getData());
                        fundspotNames.addAll(orgResponse.getOrganizationNames());
                    }
                }
                organizationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<OrganizationResponse> call, Throwable t) {
                clearAssociatedFundspot();
            }
        });

        img_uplode_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = edt_firstName.getText().toString().trim();
                String lastname = edt_lastName.getText().toString().trim();
                String location = ed_member_address.getText().toString().trim();
                String zipcode = ed_zip_code.getText().toString().trim();
                //String assocOrganization = edt_assocOrganization.getText().toString().trim();
                //String assocFundspot = edt_assocFundspot.getText().toString().trim();
                String contactInfo = edt_contactInfo.getText().toString().trim();


                int statePosition = spn_state.getSelectedItemPosition();
                int cityPosition = spn_city.getSelectedItemPosition();
               // int categoryPosition = sp_category.getSelectedItemPosition();

                Log.e("token", preference.getTokenHash());
                Log.e("userID", preference.getUserID());
                Log.e("positions", statePosition + " " + cityPosition + " "  + " ");
                if (firstname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter first name");
                } else if (firstname.length() < 2) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter first name more than 1 char");
                } else if (lastname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter last name");
                } else if (lastname.length() < 2) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter last name more than 1 char");
                } else if (location.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter your location");
                } else if (statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select state");
                } else if (cityPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select city");
                } else if (zipcode.isEmpty() || zipcode.length() < 5) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter 5 digit zip code");
                } else if (contactInfo.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter contact information");
                } else if (imagePath == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select profile image");
                } else {

                    String fundspotID = null;
                    String orgID = null;
                    if (spn_assocOrganization.getSelectedItemPosition() == 0)
                        orgID = organizationList.get(spn_assocOrganization.getSelectedItemPosition()).getUser_id();
                    if (spn_assocFundspot.getSelectedItemPosition() == 0)
                        fundspotID = fundSpotList.get(spn_assocFundspot.getSelectedItemPosition()).getUser_id();


                    dialog.show();
                    Call<VerifyResponse> generalMemberResponse = adminAPI.generalMemberProfile(preference.getUserID(), preference.getTokenHash(), firstname, lastname, location, stateItems.get(statePosition).getId(), cityItems.get(cityPosition).getId(), zipcode,orgID, fundspotID, contactInfo, ServiceGenerator.prepareFilePart("image", imagePath));
                    generalMemberResponse.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();
                            VerifyResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                    String memberData = new Gson().toJson(verifyResponse.getData().getOrganization());
                                    preference.setMemberData(memberData);

                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyResponse> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);


                        }
                    });

                }
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("General Member Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void clearAssociatedOrganization(){
        organizationList.clear();
        organizationNames.clear();
        organizationList.add(new Organization().setTitle("Select Organization"));
        organizationNames.add("Select Organization");
        organizationAdapter.notifyDataSetChanged();
    }

    private void clearAssociatedFundspot(){
        fundSpotList.clear();
        fundspotNames.clear();
        fundSpotList.add(new Organization().setTitle("Select Fundspot"));
        fundspotNames.add("Select Fundspot");
        fundspotAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri dataUri = data.getData();

                Log.e("URI", dataUri.toString());

                imagePath = FilePath.getPath(this, dataUri);
                Picasso.with(this)
                        .load(new File(imagePath))
                        .into(img_uplode_photo);
                img_remove.setVisibility(View.VISIBLE);
            } else {
                C.INSTANCE.showToast(getApplication(), "Sorry, image not found");
            }
        }
    }
    private void clearStates() {
        stateNames.clear();
        stateItems.clear();

        stateItems.add(new AreaItem("Select State"));
        stateNames.add("Select State");

        stateAdapter.notifyDataSetChanged();
    }
    private void clearCities() {
        cityNames.clear();
        cityItems.clear();

        cityItems.add(new AreaItem("Select City"));
        cityNames.add("Select City");

        cityAdapter.notifyDataSetChanged();
    }
    private void loadCities(String stateID) {
        dialog.show();
        Call<AreaResponse> cityCall = adminAPI.getCityList(stateID);
        cityCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                dialog.dismiss();
                clearCities();
                AreaResponse areaResponse = response.body();
                if (areaResponse != null) {
                    if (areaResponse.isStatus()) {
                        cityItems.addAll(areaResponse.getData());
                        cityNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
                cityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();
                clearCities();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (firstTime) {
            System.exit(0);
        } else {
            finish();
        }
    }

}
