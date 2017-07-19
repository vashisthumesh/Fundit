package com.fundit.apis;

import com.fundit.a.W;
import com.fundit.model.AppModel;
import com.fundit.model.AreaResponse;
import com.fundit.model.CategoryResponse;
import com.fundit.model.RegisterResponse;
import com.fundit.model.UniqueEmailResponse;
import com.fundit.model.VerifyResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public interface AdminAPI {

    @POST(W.COUNTRY_LIST)
    Call<AreaResponse> getCountryList();

    @FormUrlEncoded
    @POST(W.STATE_LIST)
    Call<AreaResponse> getStateList(@Field("country_id") String countryID);

    @FormUrlEncoded
    @POST(W.CITY_LIST)
    Call<AreaResponse> getCityList(@Field("state_id") String stateID);

    @POST(W.SCHOOL_TYPE)
    Call<AreaResponse> getSchoolType();

    @FormUrlEncoded
    @POST(W.SCHOOL_SUBTYPE)
    Call<AreaResponse> getSchoolSubType(@Field("type_id") String typeID);

    @POST(W.CATEGORY_LIST)
    Call<CategoryResponse> getCategoryList();

    @FormUrlEncoded
    @POST(W.UNIQUE_EMAIL)
    Call<UniqueEmailResponse> checkUniqueMail(@Field("email_id") String emailID);

    @FormUrlEncoded
    @POST(W.REGISTER_USER)
    Call<RegisterResponse> registerUser(@Field("role_id") String role_id,@Field("title") String title,@Field("first_name") String first_name,@Field("last_name")String last_name,@Field("email_id")String email_id,@Field("password")String password, @Field("anroid_device_id") String deviceID);

    @FormUrlEncoded
    @POST(W.USER_LOGIN)
    Call<VerifyResponse> signInUser(@Field("email_id")String email_id,@Field("password")String password,@Field("anroid_device_id")String anroid_device_id );

    @FormUrlEncoded
    @POST(W.FORGET_PASSWORD)
    Call<AppModel> forgetPassword(@Field("email_id")String email_id);

    @FormUrlEncoded
    @POST(W.USER_VERIFICATION)
    Call<VerifyResponse> userVerification(@Field("user_id")String user_id,@Field("otp")String otp);

    @Multipart
    @POST(W.EDIT_ORGANIZATION_PROFILE)
    Call<VerifyResponse> editOrganizationProfile(@Part("user_id") String userID, @Part("tokenhash") String token, @Part("state_id") String stateID, @Part("city_id") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("type_id") String typeID, @Part("sub_type_id") String subSchoolID, @Part("description") String description, @Part("contact_info") String contactInfo, @Part MultipartBody.Part profileImage);

}
