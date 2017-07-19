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
import com.fundit.model.User;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationProfileActivity extends AppCompatActivity {

    EditText edt_title, edt_address1, edt_zipCode, edt_description, edt_contactInfo;
    Spinner spn_state, spn_city, spn_schoolType, spn_schoolSubType;
    ImageView img_profilePic;
    Button btn_update;

    ArrayList<String> stateNames = new ArrayList<>();
    ArrayList<AreaItem> stateItems = new ArrayList<>();
    ArrayList<String> cityNames = new ArrayList<>();
    ArrayList<AreaItem> cityItems = new ArrayList<>();
    ArrayList<String> schoolNames = new ArrayList<>();
    ArrayList<AreaItem> schoolItems = new ArrayList<>();
    ArrayList<String> subSchoolNames = new ArrayList<>();
    ArrayList<AreaItem> subSchoolItems = new ArrayList<>();
    ArrayAdapter<String> stateAdapter, cityAdapter, schoolAdapter, subSchoolAdapter;

    CustomDialog dialog;

    AdminAPI adminAPI;
    AppPreference preference;

    String imagePath = null;

    int IMAGE_REQUEST = 145;

    boolean firstTime = false;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);

        Intent intent = getIntent();
        firstTime = intent.getBooleanExtra("firstTime", false);
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);

        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        fetchIDs();
    }

    private void fetchIDs() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_address1 = (EditText) findViewById(R.id.edt_address1);
        edt_zipCode = (EditText) findViewById(R.id.edt_zipCode);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_contactInfo = (EditText) findViewById(R.id.edt_contactInfo);

        spn_state = (Spinner) findViewById(R.id.spn_state);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_schoolType = (Spinner) findViewById(R.id.spn_schoolType);
        spn_schoolSubType = (Spinner) findViewById(R.id.spn_schoolSubType);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, cityNames);

        spn_state.setAdapter(stateAdapter);
        spn_city.setAdapter(stateAdapter);

        img_profilePic = (ImageView) findViewById(R.id.img_profilePic);

        btn_update = (Button) findViewById(R.id.btn_update);

        if (firstTime) {
            edt_title.setText(user.getTitle());
        }

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
        spn_schoolType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    clearSchools(false);
                } else {
                    loadSubSchools(schoolItems.get(position).getId());
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

        Call<AreaResponse> schoolCall = adminAPI.getSchoolType();
        schoolCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                clearSchools(true);
                AreaResponse areaResponse = response.body();
                if (areaResponse != null) {
                    if (areaResponse.isStatus()) {
                        schoolItems.addAll(areaResponse.getData());
                        schoolNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                }
                schoolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                clearSchools(true);
            }
        });

        img_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_title.getText().toString().trim();
                String address1 = edt_address1.getText().toString().trim();
                String zipcode = edt_zipCode.getText().toString().trim();
                String description = edt_description.getText().toString().trim();
                String contactInfo = edt_contactInfo.getText().toString().trim();

                int statePosition = spn_state.getSelectedItemPosition();
                int cityPosition = spn_city.getSelectedItemPosition();
                int schoolPosition = spn_schoolType.getSelectedItemPosition();
                int subSchoolPosition = spn_schoolSubType.getSelectedItemPosition();

                if (title.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter organization title");
                } else if (address1.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter location");
                } else if (statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select state");
                } else if (cityPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select city");
                } else if (zipcode.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter zip code");
                } else if (schoolPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select school type");
                } else if (subSchoolPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select school");
                } else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter description");
                } else if (contactInfo.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter contact information");
                } else if (imagePath == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select profile image");
                } else {
                    dialog.show();
                    Call<VerifyResponse> profileResponse = adminAPI.editOrganizationProfile(preference.getUserID(), preference.getTokenHash(), stateItems.get(statePosition).getId(), cityItems.get(cityPosition).getId(), address1, zipcode, schoolItems.get(schoolPosition).getId(), subSchoolItems.get(subSchoolPosition).getId(), description, contactInfo, ServiceGenerator.prepareFilePart("image", imagePath));
                    profileResponse.enqueue(new Callback<VerifyResponse>() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri dataUri = data.getData();

                Log.e("URI", dataUri.toString());

                imagePath = FilePath.getPath(this, dataUri);
                Picasso.with(this)
                        .load(new File(imagePath))
                        .into(img_profilePic);
            } else {
                C.INSTANCE.showToast(getApplication(), "Sorry, image not found");
            }
        }
    }

    private void loadSubSchools(String schoolID) {
        dialog.show();
        Call<AreaResponse> subSchoolCall = adminAPI.getSchoolSubType(schoolID);
        subSchoolCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                dialog.dismiss();
                clearSchools(false);
                AreaResponse areaResponse = response.body();
                if (areaResponse != null) {
                    if (areaResponse.isStatus()) {
                        subSchoolItems.addAll(areaResponse.getData());
                        subSchoolNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
                subSchoolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();
                clearSchools(false);
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
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

    private void clearCities() {
        cityNames.clear();
        cityItems.clear();

        cityItems.add(new AreaItem("Select City"));
        cityNames.add("Select City");

        cityAdapter.notifyDataSetChanged();
    }

    private void clearSchools(boolean parentType) {
        if (parentType) {
            schoolNames.clear();
            schoolItems.clear();

            schoolItems.add(new AreaItem("Type of School"));
            schoolNames.add("Type of School");

            schoolAdapter.notifyDataSetChanged();
        } else {
            subSchoolItems.clear();
            subSchoolNames.clear();

            subSchoolItems.add(new AreaItem("Select School"));
            subSchoolNames.add("Select School");

            subSchoolAdapter.notifyDataSetChanged();
        }
    }

    private void clearStates() {
        stateNames.clear();
        stateItems.clear();

        stateItems.add(new AreaItem("Select State"));
        stateNames.add("Select State");

        stateAdapter.notifyDataSetChanged();
    }
}
