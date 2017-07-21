package com.fundit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.helper.FilePath;
import com.fundit.model.AreaItem;
import com.fundit.model.AreaResponse;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralMemberProfileActivity extends AppCompatActivity {

    EditText edt_firstName,edt_lastName,edt_assocOrganization,edt_assocFundspot,edt_contactInfo,ed_member_address,ed_zip_code;
    ImageView img_uplode_photo;
    Button btn_updateProfile;
    Spinner spn_state, spn_city;
    ArrayList<String> stateNames=new ArrayList<>();
    ArrayList<AreaItem> stateItems=new ArrayList<>();
    ArrayList<String> cityNames=new ArrayList<>();
    ArrayList<AreaItem> cityItems=new ArrayList<>();
    ArrayAdapter<String> stateAdapter, cityAdapter;
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


        fetchIDs();

    }

    private void fetchIDs() {
        edt_firstName = (EditText) findViewById(R.id.edt_firstName);
        edt_lastName = (EditText) findViewById(R.id.edt_lastName);
        edt_assocOrganization = (EditText) findViewById(R.id.edt_assocOrganization);
        edt_assocFundspot = (EditText) findViewById(R.id.edt_assocFundspot);
        edt_contactInfo = (EditText) findViewById(R.id.edt_contactInfo);
        ed_member_address=(EditText)findViewById(R.id.ed_member_address);
        ed_zip_code=(EditText)findViewById(R.id.ed_zip_code);


        img_uplode_photo = (ImageView) findViewById(R.id.img_profilePic);

        btn_updateProfile = (Button) findViewById(R.id.btn_updateProfile);

        spn_state = (Spinner) findViewById(R.id.spn_state);
        spn_city = (Spinner) findViewById(R.id.spn_city);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, cityNames);

        spn_state.setAdapter(stateAdapter);
        spn_city.setAdapter(stateAdapter);

        dialog.show();

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
                String assocOrganization = edt_assocOrganization.getText().toString().trim();
                String assocFundspot = edt_assocFundspot.getText().toString().trim();
                String contactInfo = edt_contactInfo.getText().toString().trim();


                int statePosition = spn_state.getSelectedItemPosition();
                int cityPosition = spn_city.getSelectedItemPosition();
               // int categoryPosition = sp_category.getSelectedItemPosition();

                Log.e("token", preference.getTokenHash());
                Log.e("userID", preference.getUserID());
                Log.e("positions", statePosition + " " + cityPosition + " "  + " ");
                if (firstname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Your first name");
                } else if (lastname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter last name");
                } else if (location.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter your location");
                }
                else if (statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select state");
                } else if (cityPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select city");
                } else if (zipcode.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter zip code");
                }else if(assocOrganization.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Associated Organization");
                }
                else if(assocFundspot.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Associated fundspot");
                }
                else if (contactInfo.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter contact information");
                } else if (imagePath == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select profile image");
                }
                else {

                    dialog.show();
                    Call<VerifyResponse> generalMemberResponse = adminAPI.generalMemberProfile(preference.getUserID(), preference.getTokenHash(),firstname,lastname,location, stateItems.get(statePosition).getId(), cityItems.get(cityPosition).getId(), zipcode,assocOrganization,assocFundspot, contactInfo, ServiceGenerator.prepareFilePart("image", imagePath));
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri dataUri = data.getData();

                Log.e("URI", dataUri.toString());

                imagePath = FilePath.getPath(this, dataUri);
                Picasso.with(this)
                        .load(new File(imagePath))
                        .into(img_uplode_photo);
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
}
