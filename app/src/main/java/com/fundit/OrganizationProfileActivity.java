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
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.helper.FilePath;
import com.fundit.model.AreaItem;
import com.fundit.model.AreaResponse;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationProfileActivity extends AppCompatActivity {

    EditText edt_title, edt_address1, edt_zipCode, edt_description, edt_contactInfo,edt_contactInfo_email , tv_city;
    Spinner spn_state, spn_city, spn_schoolType, spn_schoolSubType;
    EditText tv_login_email;
    ImageView img_profilePic, img_remove;
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
    boolean editMode = false;

    User user = new User();

    Member member = new Member();

    Organization organization = new Organization();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);

        Intent intent = getIntent();
        firstTime = intent.getBooleanExtra("firstTime", false);
        editMode = intent.getBooleanExtra("isEdiMode", false);
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);

        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);


            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        fetchIDs();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);
        actionTitle.setText("Organization Profile");
        setSupportActionBar(toolbar);
        if(editMode) {


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        }
    }

    private void fetchIDs() {


        tv_login_email = (EditText) findViewById(R.id.tv_login_email);

        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_address1 = (EditText) findViewById(R.id.edt_address1);
        edt_zipCode = (EditText) findViewById(R.id.edt_zipCode);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_contactInfo = (EditText) findViewById(R.id.edt_contactInfo);
        edt_contactInfo_email= (EditText) findViewById(R.id.edt_contactInfo_email);
        tv_city= (EditText) findViewById(R.id.tv_city);

        spn_state = (Spinner) findViewById(R.id.spn_state);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_schoolType = (Spinner) findViewById(R.id.spn_schoolType);
        spn_schoolSubType = (Spinner) findViewById(R.id.spn_schoolSubType);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, cityNames);
        schoolAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, schoolNames);
        subSchoolAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, subSchoolNames);

        spn_state.setAdapter(stateAdapter);
        spn_city.setAdapter(cityAdapter);
        spn_schoolType.setAdapter(schoolAdapter);
        spn_schoolSubType.setAdapter(subSchoolAdapter);


        img_profilePic = (ImageView) findViewById(R.id.img_profilePic);
        img_remove = (ImageView) findViewById(R.id.img_remove);

        tv_login_email.setText(user.getEmail_id());
        edt_contactInfo_email.setText(user.getEmail_id());
        tv_login_email.setEnabled(false);

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.img)
                        .into(img_profilePic);
                imagePath = null;
                img_remove.setVisibility(View.GONE);
            }
        });

        btn_update = (Button) findViewById(R.id.btn_update);

        if (firstTime) {
            edt_title.setText(user.getTitle());
        }

        if (editMode) {

            tv_login_email.setVisibility(View.GONE);
            edt_title.setText(user.getTitle());
            edt_title.setEnabled(false);

            edt_address1.setText(member.getLocation());

            imagePath = W.FILE_URL + member.getImage();

            if (!imagePath.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(imagePath)
                        .into(img_profilePic);

                img_remove.setVisibility(View.VISIBLE);
            }
            edt_contactInfo.setText(member.getContact_info_mobile());
            edt_contactInfo_email.setText(member.getContact_info_email());
            edt_description.setText(organization.getDescription());
            edt_zipCode.setText(member.getZip_code());
            tv_city.setText(member.getCity_name());
        }


        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                   // clearCities();
                } else {
                   // loadCities(stateItems.get(position).getId());
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
                  //  clearSchools(false);
                } else {
                  //  loadSubSchools(schoolItems.get(position).getId());
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

                if (!firstTime && editMode) {
                    checkForSelectedState();
                }


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
                        Log.e("check" , "--->");
                        schoolItems.addAll(areaResponse.getData());
                        schoolNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                }
                schoolAdapter.notifyDataSetChanged();

                if (!firstTime && editMode) {

                    checkForSelectedSchoolType();

                }
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
                String contact_email=edt_contactInfo_email.getText().toString().trim();
                String cityName = tv_city.getText().toString().trim();

                int statePosition = spn_state.getSelectedItemPosition();
             //   int cityPosition = spn_city.getSelectedItemPosition();
              //  int schoolPosition = spn_schoolType.getSelectedItemPosition();
               // int subSchoolPosition = spn_schoolSubType.getSelectedItemPosition();

                Log.e("token", preference.getTokenHash());
                Log.e("userID", preference.getUserID());

                Log.e("sizes", stateItems.size() + " " + cityItems.size() + " " + schoolItems.size() + " " + subSchoolItems.size());

                if (title.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter organization title");
                } else if (title.length() < 2) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter organization title more than 1 char");
                } else if (address1.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter address");
                } else if (statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select state");
                } else if (cityName.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter city");
                } else if (zipcode.isEmpty() || zipcode.length() < 5) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter zip code");
                } /*else if (schoolPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select school type");
                } else if (subSchoolPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select school");
                }*/ else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter description");
                }else if (!contactInfo.isEmpty() && contactInfo.length() != 10) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid contact information");
                }
                else if(contact_email.isEmpty())
                {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter contact information");
                }
                else if(!C.INSTANCE.validEmail(contact_email))
                {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid email");
                } else if (imagePath == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select profile image");
                } else {
                    dialog.show();
                    Call<VerifyResponse> profileResponse = null;

                    if (!imagePath.startsWith("http")) {

                        profileResponse = adminAPI.editOrganizationProfile(preference.getUserID(), preference.getTokenHash(), title, stateItems.get(statePosition).getId(), cityName, address1, zipcode, "", "", description, contact_email,contactInfo, ServiceGenerator.prepareFilePart("image", imagePath));
                    } else {

                        profileResponse = adminAPI.editTimeOrganizationProfile(preference.getUserID(), preference.getTokenHash(), title, stateItems.get(statePosition).getId(), cityName , address1, zipcode, "", "", description, contact_email,contactInfo);


                    }

                    Log.e("cityName" , "-->" + cityName);

                    profileResponse.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();
                            VerifyResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                    String memberData = new Gson().toJson(verifyResponse.getData().getMember().getOrganization());
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

    private void checkForSelectedSchoolType() {

        int pos = 0;
        for (int i = 0; i < schoolItems.size(); i++) {

            if (schoolItems.get(i).getId().equals(organization.getType_id())) {

                pos = i;
                break;


            }


        }
        spn_schoolType.setSelection(pos);


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
                img_remove.setVisibility(View.VISIBLE);
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

                if (!firstTime && editMode) {

                    checkforSelectedSubType();

                }


            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();
                clearSchools(false);
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }

    private void checkforSelectedSubType() {

        int pos = 0;
        for (int i = 0; i < subSchoolItems.size(); i++) {

            if (subSchoolItems.get(i).getId().equals(organization.getSub_type_id())) {

                pos = i;
                break;

            }


        }
        spn_schoolSubType.setSelection(pos);


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
                if (!firstTime && editMode) {

                    checkforSelectedCity();
                }
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

    @Override
    public void onBackPressed() {
        if (firstTime) {
            System.exit(0);
        } else {
            finish();
        }

    }


    private void checkForSelectedState() {
        int pos = 0;
        for (int i = 0; i < stateItems.size(); i++) {
            if (stateItems.get(i).getId().equals(member.getState_id())) {
                pos = i;
                break;
            }
        }
        spn_state.setSelection(pos);
    }

    private void checkforSelectedCity() {

        int pos = 0;
        for (int i = 0; i < cityItems.size(); i++) {
            if (cityItems.get(i).getId().equals(member.getCity_id())) {

                pos = i;
                break;
            }
        }
        spn_city.setSelection(pos);
    }


}
