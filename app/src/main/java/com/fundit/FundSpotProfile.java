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
import com.fundit.model.CategoryResponse;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundSpotProfile extends AppCompatActivity {


    EditText ed_fund_title, ed_fund_address, ed_fund_zipcode, ed_fund_fundsplit, ed_fund_description, ed_fund_contact_info;
    Spinner sp_state, sp_city, sp_category;
    ImageView img_uplode_photo, img_remove;
    Button bt_update_profile;

    TextView tv_login_email;


    ArrayList<String> stateNames = new ArrayList<>();
    ArrayList<AreaItem> stateItems = new ArrayList<>();
    ArrayList<String> cityNames = new ArrayList<>();
    ArrayList<AreaItem> cityItems = new ArrayList<>();
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayList<AreaItem> categoryItems = new ArrayList<>();

    ArrayAdapter<String> stateAdapter, cityAdapter, categoryAdapter;

    CustomDialog dialog;

    AdminAPI adminAPI;
    AppPreference preference;

    String imagePath = null;

    int IMAGE_REQUEST = 145;

    boolean firstTime = false;
    boolean editMode = false;

    User user = new User();

    Member member = new Member();

    Fundspot fundspot = new Fundspot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_spot_profile);
        Intent intent = getIntent();
        firstTime = intent.getBooleanExtra("firstTime", false);
        editMode = intent.getBooleanExtra("isEditMode", false);
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        fetchid();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Fundspot Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchid() {
        tv_login_email = (TextView) findViewById(R.id.tv_login_email);


        ed_fund_title = (EditText) findViewById(R.id.ed_fund_title);
        ed_fund_address = (EditText) findViewById(R.id.ed_fund_address);
        ed_fund_zipcode = (EditText) findViewById(R.id.ed_fund_zipcode);
        ed_fund_fundsplit = (EditText) findViewById(R.id.ed_fund_fundsplit);
        ed_fund_description = (EditText) findViewById(R.id.ed_fund_description);
        ed_fund_contact_info = (EditText) findViewById(R.id.ed_fund_contact_info);

        tv_login_email = (TextView) findViewById(R.id.tv_login_email);


        tv_login_email.setText("Login with:" + user.getEmail_id());
        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_category = (Spinner) findViewById(R.id.sp_category);

        img_uplode_photo = (ImageView) findViewById(R.id.img_uplode_photo);
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

        bt_update_profile = (Button) findViewById(R.id.bt_update_profile);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        cityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, cityNames);
        categoryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, categoryNames);

        sp_state.setAdapter(stateAdapter);
        sp_city.setAdapter(cityAdapter);
        sp_category.setAdapter(categoryAdapter);


        if (firstTime) {
            ed_fund_title.setText(user.getTitle());
        }
        if (editMode) {

            tv_login_email.setVisibility(View.GONE);

            ed_fund_title.setText(user.getTitle());
            ed_fund_title.setEnabled(false);

            ed_fund_address.setText(member.getLocation());

            imagePath = member.getImage();

            if(!imagePath.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(W.FILE_URL + imagePath)
                        .into(img_uplode_photo);

                img_remove.setVisibility(View.VISIBLE);
            }
            ed_fund_zipcode.setText(member.getZip_code());
            ed_fund_fundsplit.setText(fundspot.getFundraise_split());

            ed_fund_description.setText(fundspot.getDescription());
            ed_fund_contact_info.setText(member.getContact_info());

            Log.e("categeory" , "" +fundspot.getCategory_id());
        }


        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        Call<CategoryResponse> categoryCall = adminAPI.getCategoryList();
        categoryCall.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                categoryItems.add(new AreaItem("Select Category"));
                categoryNames.add("Select Category");
                CategoryResponse areaResponse = response.body();
                if (areaResponse != null) {
                    if (areaResponse.isStatus()) {
                        categoryItems.addAll(areaResponse.getData());
                        categoryNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                }
                categoryAdapter.notifyDataSetChanged();

                if(!firstTime && editMode){
                    checkforSelectedCategory();
                }


            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                categoryItems.add(new AreaItem("Select Category"));
                categoryNames.add("Select Category");
                categoryAdapter.notifyDataSetChanged();
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

        bt_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ed_fund_title.getText().toString().trim();
                String address1 = ed_fund_address.getText().toString().trim();
                String zipcode = ed_fund_zipcode.getText().toString().trim();
                String funsplit = ed_fund_fundsplit.getText().toString().trim();
                String description = ed_fund_description.getText().toString().trim();
                String contactInfo = ed_fund_contact_info.getText().toString().trim();

                int statePosition = sp_state.getSelectedItemPosition();
                int cityPosition = sp_city.getSelectedItemPosition();
                int categoryPosition = sp_category.getSelectedItemPosition();

                Log.e("token", preference.getTokenHash());
                Log.e("userID", preference.getUserID());
                Log.e("positions", statePosition + " " + cityPosition + " " + categoryPosition + " ");
                if (title.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Funspot title");
                } else if (title.length() < 2) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Funspot title more than 1 char");
                } else if (address1.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter location");
                } else if (statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select state");
                } else if (cityPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select city");
                } else if (zipcode.isEmpty() || zipcode.length() < 5) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter 5 digit zip code");
                } else if (categoryPosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select category");
                } else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter description");
                } else if (contactInfo.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter contact information");
                } else if (imagePath == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select profile image");
                } else if (funsplit.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Enter Funsplit");
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user_id",preference.getUserID());
                    intent.putExtra("tokken_hash",preference.getUserID());
                    intent.putExtra("title",title);
                    intent.putExtra("state_id",stateItems.get(statePosition).getId());
                    intent.putExtra("city_id",cityItems.get(cityPosition).getId());
                    intent.putExtra("address1",address1);
                    intent.putExtra("zipcode",zipcode);
                    intent.putExtra("category",categoryItems.get(categoryPosition).getId());
                    intent.putExtra("funsplit",funsplit);
                    intent.putExtra("description",description);
                    intent.putExtra("contactInfo",contactInfo);
                    intent.putExtra("imagePath",imagePath);

                    startActivity(intent);
                    /*dialog.show();
                    Call<VerifyResponse> fundspotResponse = adminAPI.editFundsportProfile(preference.getUserID(), preference.getTokenHash(), title, stateItems.get(statePosition).getId(), cityItems.get(cityPosition).getId(), address1, zipcode, categoryItems.get(categoryPosition).getId(), funsplit, description, contactInfo, ServiceGenerator.prepareFilePart("image", imagePath));
                    fundspotResponse.enqueue(new Callback<VerifyResponse>() {
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
                    });*/
                }
            }
        });
    }

    private void checkforSelectedCategory() {

        int pos = 0;
        for (int i=0; i<categoryItems.size();i++){
            if(categoryItems.get(i).getId().equals(fundspot.getCategory_id())){
                pos=i;
                break;

            }


        }

        sp_category.setSelection(pos);


    }

    private void checkForSelectedState() {
        int pos = 0;
        for (int i = 0; i < stateItems.size(); i++) {
            if (stateItems.get(i).getId().equals(member.getState_id())) {
                pos = i;
                break;
            }
        }
        sp_state.setSelection(pos);
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

    private void clearCities() {
        cityNames.clear();
        cityItems.clear();

        cityItems.add(new AreaItem("Select City"));
        cityNames.add("Select City");

        cityAdapter.notifyDataSetChanged();
    }


    private void clearStates() {
        stateNames.clear();
        stateItems.clear();

        stateItems.add(new AreaItem("Select State"));
        stateNames.add("Select State");

        stateAdapter.notifyDataSetChanged();
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

                if(!firstTime && editMode){

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

    private void checkforSelectedCity() {

        int pos=0;
        for(int i=0 ; i<cityItems.size();i++){

            if(cityItems.get(i).getId().equals(member.getCity_id())){

                pos=i;
                break;
            }

        }

        sp_city.setSelection(pos);





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
