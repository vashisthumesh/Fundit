package com.fundit.apis;

import android.provider.ContactsContract;

import com.fundit.a.W;
import com.fundit.model.AppModel;
import com.fundit.model.AreaResponse;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.CategoryResponse;
import com.fundit.model.ForgotPasswordEmailResponse;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.MemberListResponse;
import com.fundit.model.OrganizationResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.RegisterResponse;
import com.fundit.model.UniqueEmailResponse;
import com.fundit.model.VerifyResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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
    Call<RegisterResponse> registerUser(@Field("role_id") String role_id, @Field("title") String title, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email_id") String email_id, @Field("password") String password, @Field("anroid_device_id") String deviceID);

    @FormUrlEncoded
    @POST(W.USER_LOGIN)
    Call<VerifyResponse> signInUser(@Field("email_id") String email_id, @Field("password") String password, @Field("anroid_device_id") String anroid_device_id);

    @FormUrlEncoded
    @POST(W.FORGET_PASSWORD)
    Call<AppModel> forgetPassword(@Field("email_id") String email_id);

    @FormUrlEncoded
    @POST(W.USER_VERIFICATION)
    Call<VerifyResponse> userVerification(@Field(W.KEY_USERID) String user_id, @Field("otp") String otp);

    @Multipart
    @POST(W.EDIT_ORGANIZATION_PROFILE)
    Call<VerifyResponse> editOrganizationProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_id") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("type_id") String typeID, @Part("sub_type_id") String subSchoolID, @Part("description") String description, @Part("contact_info") String contactInfo, @Part MultipartBody.Part profileImage);

    @Multipart
    @POST(W.EDIT_FUNDSPOT_PROFILE)
    Call<VerifyResponse> editFundsportProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_id") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("category_id") String category_id, @Part("fundraise_split") String fundraise_split, @Part("description") String description, @Part("contact_info") String contactInfo, @Part MultipartBody.Part profileImage);

    @Multipart
    @POST(W.EDIT_GENERALMEMBER)
    Call<VerifyResponse> generalMemberProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("first_name") String first_name, @Part("last_name") String last_name, @Part("location") String location, @Part("state_id") String stateID, @Part("city_id") String cityID, @Part("zip_code") String zipCode, @Part("organization_id") String organization_id, @Part("fundspot_id") String fundspot_id, @Part("contact_info") String contact_info, @Part MultipartBody.Part profileImage);

    @Multipart
    @POST(W.ADD_PRODUCT)
    Call<AppModel> addMyProduct(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("type_id") String typeID, @Part("name") String name, @Part("description") String description, @Part("price") String price, @Part("fine_print") String fine_print, @Part MultipartBody.Part productImage);

    @Multipart
    @POST(W.EDIT_PRODUCT)
    Call<AppModel> editMyProduct(@Part("id") String productID, @Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("type_id") String typeID, @Part("name") String name, @Part("description") String description, @Part("price") String price,@Part("fine_print") String fine_print, @Part MultipartBody.Part productImage);

    @FormUrlEncoded
    @POST(W.LIST_PRODUCT)
    Call<ProductListResponse> getProductList(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(W.DELETE_PRODUCT)
    Call<AppModel> deleteProduct(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("id") String productID);

    @FormUrlEncoded
    @POST(W.SEARCH_PRODUCT)
    Call<ProductListResponse> searchProduct(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("name") String filterName);

    @FormUrlEncoded
    @POST(W.BROWSE_FUNDSPOT)
    Call<FundspotListResponse> browseFundspots(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(W.FUNDSPOT_PRODUCT)
    Call<ProductListResponse> fundSpotProducts(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("fundspot_id") String fundspotID, @Field("type_id") String typeID);

    @FormUrlEncoded
    @POST(W.FUNDSPOT_SEARCH)
    Call<FundspotListResponse> searchFundspot(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("title") String title);

    @FormUrlEncoded
    @POST(W.ADD_CAMPAIGN)
    Call<AppModel> addCampaign(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("campaign_detail") String campaignDetailArray, @Field("campaign_member") String memberIDArray);

    @POST(W.ALL_ORGANIZATION)
    Call<OrganizationResponse> getAllOrganizations();

    @POST(W.ALL_FUNDSPOT)
    Call<OrganizationResponse> getAllFundspots();

    @FormUrlEncoded
    @POST(W.CAMPAIGN_LIST)
    Call<CampaignListResponse> getAllCampaigns(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("role_id") String roleID, @Field("organization_id") String organizationID, @Field("fundspot_id") String fundspotID,@Field("status")String status,@Field("action_status")String action_status);

    @FormUrlEncoded
    @POST(W.MEMBER_LIST)
    Call<MemberListResponse> getAllMemberList(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("role_id") String roleID, @Field("organization_id") String organizationID, @Field("fundspot_id") String fundspotID);


    @FormUrlEncoded
    @POST(W.CANCEL_CAMPAIGN)
    Call<AppModel> cancelCampaign(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token,@Field("campaign_id") String campaign_id , @Field("role_id") String role_id, @Field("action_status") String status);


    @FormUrlEncoded
    @POST(W.APP_EDIT_CAMPAIGN)
    Call<AppModel> appEditcampaign(@Field("user_id") String user_id,@Field("tokenhash") String tokenhash,@Field("campaign_id") String campaign_id, @Field("campaign_detail") String campaignDetailArray, @Field("campaign_member") String memberIDArray);

    @FormUrlEncoded
    @POST(W.ForgetPass)
    Call<ForgotPasswordEmailResponse> ForgetPass(@Field("email_id")String email_id);

    @FormUrlEncoded
    @POST(W.ForgetPass_OTP)
    Call<ForgotPasswordEmailResponse> ForgetPass_otp(@Field("user_id")String user_id,@Field("otp")String otp);

    @FormUrlEncoded
    @POST(W.ForgetPass_edit_change_password)
    Call<AppModel> ForgetPass_change_edit(@Field("user_id")String user_id,@Field("password")String otp);
}
