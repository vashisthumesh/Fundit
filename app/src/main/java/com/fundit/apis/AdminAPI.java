package com.fundit.apis;

import android.provider.ContactsContract;

import com.fundit.a.W;
import com.fundit.model.Address;
import com.fundit.model.AppModel;
import com.fundit.model.App_Single_Fundspot;
import com.fundit.model.App_Single_Organization;
import com.fundit.model.AreaResponse;
import com.fundit.model.BankCardResponse;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.CategoryResponse;
import com.fundit.model.CompleteOrderModel;
import com.fundit.model.ForgotPasswordEmailResponse;
import com.fundit.model.FundRequest;
import com.fundit.model.Fundspot;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.FundspotListResponseFundspot;
import com.fundit.model.GeneralMemberProfileResponse;
import com.fundit.model.GetDataResponses;
import com.fundit.model.GetSearchPeople;
import com.fundit.model.InboxMessagesResponse;
import com.fundit.model.JoinMemberModel;
import com.fundit.model.MemberListResponse;
import com.fundit.model.MemberResponse;
import com.fundit.model.News_model;
import com.fundit.model.NotificationCampaignModel;
import com.fundit.model.NotificationSettingsModel;
import com.fundit.model.OrderHistoryResponse;
import com.fundit.model.OrganizationResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.QRScanModel;
import com.fundit.model.RegisterResponse;
import com.fundit.model.UniqueEmailResponse;
import com.fundit.model.VerifyResponse;

import java.util.StringTokenizer;

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
    Call<VerifyResponse> editOrganizationProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_name") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("type_id") String typeID, @Part("sub_type_id") String subSchoolID, @Part("description") String description, @Part("contact_info_email") String contact_email,@Part("contact_info_mobile") String contactInfo, @Part MultipartBody.Part profileImage);


    @Multipart
    @POST(W.EDIT_ORGANIZATION_PROFILE)
    Call<VerifyResponse> editTimeOrganizationProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_name") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("type_id") String typeID, @Part("sub_type_id") String subSchoolID, @Part("description") String description, @Part("contact_info_email") String contact_email,@Part("contact_info_mobile") String contactInfo);


    @Multipart
    @POST(W.EDIT_FUNDSPOT_PROFILE)
    Call<VerifyResponse> editFundsportProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_name") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("category_id") String category_id, @Part("fundraise_split") String fundraise_split, @Part("description") String description, @Part("contact_info") String contactInfo,@Part("fundspot_percent") String fundspot_percent,@Part("organization_percent") String organization_percent,@Part("campaign_duration") String campaign_duration,@Part("max_limit_of_coupon_price") String max_limit_of_coupon_price,@Part("coupon_expire_day") String coupon_expire_day , @Part("split_visibility") String splitVisibility , @Part MultipartBody.Part profileImage);


    @Multipart
    @POST(W.EDIT_FUNDSPOT_PROFILE)
    Call<VerifyResponse> firstTimeEditFundsportProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, /*@Part("city_id") String cityID*/ @Part("city_name") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("category_id") String category_id, @Part("fundraise_split") String fundraise_split, @Part("description") String description, @Part("contact_info_email") String contact_email,@Part("contact_info_mobile") String contactInfo, @Part MultipartBody.Part profileImage);

    @Multipart
    @POST(W.EDIT_FUNDSPOT_PROFILE)
    Call<VerifyResponse> withoutImageEditFundsportProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("title") String title, @Part("state_id") String stateID, @Part("city_id") String cityID, @Part("location") String address, @Part("zip_code") String zipCode, @Part("category_id") String category_id, @Part("fundraise_split") String fundraise_split, @Part("description") String description, @Part("contact_info_email") String contact_email,@Part("contact_info_mobile") String contactInfo);



    @Multipart
    @POST(W.EDIT_FUNDSPOT_PROFILE)
    Call<VerifyResponse> onlyCampaignEdit(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash,@Part("fundspot_percent") String fundspot_percent,@Part("organization_percent") String organization_percent,@Part("campaign_duration") String campaign_duration,@Part("max_limit_of_coupon_price") String max_limit_of_coupon_price,@Part("coupon_expire_day") String coupon_expire_day);




    @Multipart
    @POST(W.EDIT_GENERALMEMBER)
    Call<VerifyResponse> generalMemberProfile(@Part(W.KEY_USERID) String userID, @Part(W.KEY_TOKEN) String tokenHash, @Part("first_name") String first_name, @Part("last_name") String last_name, @Part("location") String location, @Part("state_id") String stateID, @Part("city_name") String cityID, @Part("zip_code") String zipCode, @Part("organization_id") String organization_id, @Part("fundspot_id") String fundspot_id,@Part("contact_info_email") String contact_email,@Part("contact_info_mobile") String contactInfo, @Part MultipartBody.Part profileImage);

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
    @POST(W.BROWSE_ORGANIZATION)
    Call<FundspotListResponse> browseOrganization(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token);


    @FormUrlEncoded
    @POST(W.FUNDSPOT_PRODUCT)
    Call<ProductListResponse> fundSpotProducts(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("fundspot_id") String fundspotID, @Field("type_id") String typeID);

    @FormUrlEncoded
    @POST(W.FUNDSPOT_SEARCH)
    Call<FundspotListResponse> searchFundspot(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("title") String title);


    @FormUrlEncoded
    @POST(W.FUNDSPOT_SEARCH_FUNDSPOT)
    Call<GetDataResponses> searchFundspotOne(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("title") String title, @Field("city_id") String city_id , @Field("zip_code") String zip_code);

    @FormUrlEncoded
    @POST(W.GET_ORGANIZATION_BROWSE)
    Call<GetDataResponses> GET_ORGANIZATIONBROWSE(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("title") String title, @Field("city_id") String city_id , @Field("zip_code") String zip_code);


    @FormUrlEncoded
    @POST(W.BROWSE_ORGANIZATION)
    Call<FundspotListResponse> searchOrganization(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("title") String title);

    @FormUrlEncoded
    @POST(W.ADD_CAMPAIGN)
    Call<AppModel> addCampaign(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("campaign_detail") String campaignDetailArray, @Field("campaign_member") String memberIDArray , @Field("product_id") String selectedProductArray);

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
    @POST(W.SEARCHMEMBER_LIST)
    Call<MemberListResponse> getMemberList(@Field(W.KEY_USERID) String userID, @Field(W.KEY_TOKEN) String token, @Field("name") String name );



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


    @FormUrlEncoded
    @POST(W.GET_ALL_APPROVED_CAMPAIGN)
    Call<CampaignListResponse> ApprovedCampaign(@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenhash , @Field(W.KEY_ROLEID) String roleId , @Field("Campaign_status") String status);

    @FormUrlEncoded
    @POST(W.GET_ALL_NEWS)
    Call<News_model> NEWS_MODEL(@Field(W.KEY_USERID) String userId,@Field("page") String page);


    @FormUrlEncoded
    @POST(W.GET_ADDRESS)
    Call<Address> GetAddress(@Field("fundspot_id") String fundspot_id);

    @FormUrlEncoded
    @POST(W.GET_ALLFUNDSPOT)
    Call<App_Single_Fundspot> GetAllFundspot(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(W.GET_ALLORGANIZATION)
    Call<App_Single_Organization> GetAllOrganization(@Field("user_id") String user_id);



    @FormUrlEncoded
    @POST(W.GET_SELLER_CAMPAIGN)
    Call<CampaignListResponse> SellerCampaign(@Field("member_id") String member_id , @Field("Campaign_status") String status);

    @FormUrlEncoded
    @POST(W.GET_MEMBER_FOR_CAMPAIGN)
    Call<MemberResponse> GetMemberForCampaign(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field("campaign_id") String campaignId);

    @FormUrlEncoded
    @POST(W.DELETE_MEMBER_FROM_CAMPAIGN)
    Call<AppModel> RemoveUserFromCampaign(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field("campaign_id") String capmpaignId , @Field("member_id") String memberId);


    @FormUrlEncoded
    @POST(W.VIEW_MEMBER_PROFILE)
    Call<GeneralMemberProfileResponse>ViewGeneralMemberProfile(@Field(W.KEY_USERID) String userId,@Field(W.KEY_TOKEN) String tokenhash,@Field("member_id") String memberId);


//    @FormUrlEncoded
//    @POST(W.ADDORDER)
//    Call<AppModel> AddOrder(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field(W.KEY_TOKEN) String tokenhash , @Field("campaign_id") String capmpaignId , @Field("firstname") String firstName , @Field("lastname") String lastName , @Field("email") String emailId , @Field("mobile") String mobileNo , @Field("payment_address_1") String address , @Field("payment_city") String city , @Field("payment_postcode") String zipCode , @Field("payment_state") String state , @Field("payment_method") String method , @Field("total") String totalPrice , @Field("owner_id") String ownerId , @Field("latitude") String lattitude , @Field("longitude") String longitude , @Field("organization_id") String orgenizationId , @Field("fundspot_id") String fundspotId , @Field("product_id") String productIdArray);

    @FormUrlEncoded
    @POST(W.ADDORDER)
    Call<AppModel>AddOrder(@Field("campaign_id") String capmpaignId,@Field(W.KEY_USERID) String userId ,@Field(W.KEY_TOKEN) String tokenhash,@Field(W.KEY_ROLEID) String roleId,@Field("firstname") String firstName,@Field("lastname") String lastName ,@Field("email") String emailId,@Field("mobile") String mobileNo,@Field("payment_address_1") String address,@Field("payment_city") String city,@Field("payment_postcode") String zipCode,@Field("payment_state") String state,@Field("payment_method") String method,@Field("total") String totalPrice,@Field("owner_id") String ownerId,@Field("latitude") String lattitude,@Field("longitude") String longitude,@Field("organization_id") String orgenizationId ,@Field("fundspot_id") String fundspotId,@Field("product_id") String productIdArray,@Field("card_number") String card_number,@Field("card_type") String card_type,@Field("month") String month,@Field("year") String year,@Field("cvv") String cvv,@Field("bcard_id") String bcard_id,@Field("save_card") String save_card , @Field("on_behalf_of") String onBehalfOf , @Field("order_request") String orderRequest , @Field("Other_user") String otherUser);



    @FormUrlEncoded
    @POST(W.ADDORDER)
    Call<AppModel> AddCardOrder(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field(W.KEY_TOKEN) String tokenhash , @Field("campaign_id") String capmpaignId , @Field("firstname") String firstName , @Field("lastname") String lastName , @Field("email") String emailId , @Field("mobile") String mobileNo , @Field("payment_address_1") String address , @Field("payment_city") String city , @Field("payment_postcode") String zipCode , @Field("payment_state") String state , @Field("payment_method") String method , @Field("total") String totalPrice , @Field("owner_id") String ownerId , @Field("latitude") String lattitude , @Field("longitude") String longitude , @Field("organization_id") String orgenizationId , @Field("fundspot_id") String fundspotId , @Field("product_id") String productIdArray , @Field("card_number") String cardNumber , @Field("card_type") String cardType , @Field("month") String month , @Field("year") String year , @Field("cvv") String cvv , @Field("bcard_id") String cardId , @Field("save_card") String savedCard , @Field("on_behalf_of") String onBehalfOf , @Field("order_request") String orderRequest , @Field("Other_user") String otherUser);



    @FormUrlEncoded
    @POST(W.ADDORDER)
    Call<AppModel> AddCard_Order(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field(W.KEY_TOKEN) String tokenhash , @Field("campaign_id") String capmpaignId , @Field("firstname") String firstName , @Field("lastname") String lastName , @Field("email") String emailId , @Field("mobile") String mobileNo , @Field("payment_address_1") String address , @Field("payment_city") String city , @Field("payment_postcode") String zipCode , @Field("payment_state") String state , @Field("payment_method") String method , @Field("total") String totalPrice , @Field("owner_id") String ownerId , @Field("latitude") String lattitude , @Field("longitude") String longitude , @Field("organization_id") String orgenizationId , @Field("fundspot_id") String fundspotId , @Field("product_id") String productIdArray ,@Field("auth_cust_paymnet_profile_id") String auth_cust_paymnet_profile_id,@Field("customerProfileId") String customerProfileId, @Field("cvv") String cvv , @Field("card_id") String cardId , @Field("save_card") String savedCard , @Field("on_behalf_of") String onBehalfOf , @Field("order_request") String orderRequest , @Field("Other_user") String otherUser,@Field("is_card_save") String is_card_save);


    @FormUrlEncoded
    @POST(W.ORDER_HISTORY)
    Call<OrderHistoryResponse> OrderHistory(@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenHash);

    @FormUrlEncoded
    @POST(W.ORDER_HISTORY)
    Call<OrderHistoryResponse> GetCoupon(@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenHash , @Field("coupon_delete") String coupon);


    @FormUrlEncoded
    @POST(W.GET_CARD_DATA)
    Call<BankCardResponse> BankCard(@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenHash);



    @FormUrlEncoded
    @POST(W.FUND_ORG_REQUEST)
    Call<FundRequest> Fund_Org_Request(@Field("member_id") String member_id);



    @FormUrlEncoded
    @POST(W.ACCEPT_DECLINE_REQUEST)
    Call<AppModel> Acc_Decline_Request(@Field(W.KEY_USERID) String user_id,@Field("member_id") String member_id,@Field("status") String status,@Field("role_id") String role_id);


    @FormUrlEncoded
    @POST(W.DELETE_CARDDETAIL)
    Call<AppModel> Delete_Carddetail(@Field(W.KEY_USERID) String userId,@Field("card_id") String card_id,@Field("auth_cust_paymnet_profile_id") String auth_cust_paymnet_profile_id,@Field("customerProfileId") String customerProfileId);


    @FormUrlEncoded
    @POST(W.GET_ALL_INBOX)
    Call<InboxMessagesResponse> GetInboxMessage(@Field("receiver_id") String recieverId , @Field(W.KEY_TOKEN) String tokenHash);

    @FormUrlEncoded
    @POST(W.CLEAR_MESSAGE)
    Call<AppModel>Clear_All_Message(@Field(W.KEY_USERID)String userId,@Field(W.KEY_TOKEN) String tokenHash);

    @FormUrlEncoded
    @POST(W.READ_INBOX_MESSAGES)
    Call<AppModel> UnreadMessage (@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenHash , @Field("notification_id") String messageId);

    @FormUrlEncoded
    @POST(W.SEND_MESSAGES)
    Call<AppModel> SendMessage(@Field("sender_id") String userId , @Field(W.KEY_TOKEN) String tokenHash , @Field("receiver_id") String receiverId , @Field("subject") String subject , @Field("msg") String message);


    @FormUrlEncoded
    @POST(W.GET_ALL_MEMBERS)
    Call<MemberListResponse> MEMBER_LIST_RESPONSE_CALL (@Field(W.KEY_USERID) String userId , @Field(W.KEY_TOKEN) String tokenHash , @Field(W.KEY_ROLEID) String roleId);


    @FormUrlEncoded
    @POST(W.GET_ALL_FUND)
        Call<GetDataResponses> GET_ALL_FUNDSPOT (@Field(W.KEY_USERID) String userId , @Field("city_id") String cityId , @Field("zip_code") String zipCode);


    @FormUrlEncoded
    @POST(W.GET_ALL_ORG)
    Call<GetDataResponses> GET_ALL_ORGANIZATION (@Field(W.KEY_USERID) String userId , @Field("city_id") String cityId , @Field("zip_code") String zipCode);


    @FormUrlEncoded
    @POST(W.MESSGESUSERLIST)
    Call<MemberListResponse> getMembersMessageTime(@Field(W.KEY_USERID) String userID,  @Field("title") String title);

    @FormUrlEncoded
    @POST(W.PEOPLE_SEARCH)
    Call<GetSearchPeople> getsearchpeople(@Field(W.KEY_USERID) String userID,@Field("title") String title,@Field(W.KEY_ROLEID) String roleID);


    @FormUrlEncoded
    @POST(W.CHECK_JOIN_MEMBER)
    Call<JoinMemberModel> checkJoinMember(@Field("member_id") String memberId , @Field(W.KEY_ROLEID) String roleId , @Field(W.KEY_USERID) String userId);


    @FormUrlEncoded
    @POST(W.COUPON_REQUEST)
    Call<AppModel> DeleteCoupon(@Field("order_id") String orderId , @Field(W.KEY_USERID) String userId , @Field("request_status") String status);


    @FormUrlEncoded
    @POST(W.COUPON_REQUEST)
    Call<AppModel> AcceptCoupon(@Field("order_id") String orderId , @Field(W.KEY_USERID) String userId , @Field("request_status") String status , @Field("bcard_id") String cardId , @Field("card_number") String cardNumber , @Field("card_type") String cardType , @Field("cvv") String cvv , @Field("month") String month , @Field("year") String year , @Field("save_card") String saveCard);


    @FormUrlEncoded
    @POST(W.COUPON_REQUEST)
    Call<CompleteOrderModel> CouponAccpet(@Field("order_id") String orderId , @Field(W.KEY_USERID) String userId , @Field("request_status") String status , @Field("card_id") String cardId , @Field("cvv") String cvv , @Field("auth_cust_paymnet_profile_id") String paymentProfileId , @Field("customerProfileId") String customerProfileId , @Field("save_card") String saveCard , @Field("is_card_save") String isCardSave);



    @FormUrlEncoded
    @POST(W.GETALLSETTINGSLIST)
    Call<NotificationSettingsModel> GetAllSettings (@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId);


    @FormUrlEncoded
    @POST(W.UPDATESETTINGS)
    Call<AppModel> UpdateSettings (@Field("id") String id , @Field("status") String status);


    @FormUrlEncoded
    @POST(W.GETINDIVIDUALCAMPAIGN)
    Call<NotificationCampaignModel> NOTIFICATION_CAMPAIGN_MODEL_CALL (@Field("campaign_id") String campaignId);


    @FormUrlEncoded
    @POST(W.ADDORDER)
    Call<CompleteOrderModel> CompleteOrder(@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field(W.KEY_TOKEN) String tokenhash , @Field("campaign_id") String capmpaignId , @Field("firstname") String firstName , @Field("lastname") String lastName , @Field("email") String emailId , @Field("mobile") String mobileNo , @Field("payment_address_1") String address , @Field("payment_city") String city , @Field("payment_postcode") String zipCode , @Field("payment_state") String state , @Field("payment_method") String method , @Field("total") String totalPrice , @Field("owner_id") String ownerId , @Field("latitude") String lattitude , @Field("longitude") String longitude , @Field("organization_id") String orgenizationId , @Field("fundspot_id") String fundspotId , @Field("product_id") String productIdArray ,@Field("auth_cust_paymnet_profile_id") String auth_cust_paymnet_profile_id,@Field("customerProfileId") String customerProfileId, @Field("cvv") String cvv , @Field("card_id") String cardId , @Field("save_card") String savedCard , @Field("on_behalf_of") String onBehalfOf , @Field("order_request") String orderRequest , @Field("Other_user") String otherUser,@Field("is_card_save") String is_card_save);

    @FormUrlEncoded
    @POST(W.CHECKVALIDCOUPON)
    Call<QRScanModel> CheckValidCoupon (@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field("coupon_number") String couponNumber);

    @FormUrlEncoded
    @POST(W.CHECKVALIDCOUPON)
    Call<QRScanModel> CheckQRValidCoupon (@Field(W.KEY_USERID) String userId , @Field(W.KEY_ROLEID) String roleId , @Field("coupon_data") String couponNumber);




}
