package com.fundit.a;

import com.android.volley.Request;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class W {
    public static final int POST = Request.Method.POST;

    public static final String KEY_USERID = "user_id";
    public static final String KEY_TOKEN = "tokenhash";
    public static final String KEY_ROLEID = "role_id";

    // public static final String BASE_URL = "http://app.nivida.in/fundit/"; //Online URL
    public static final String BASE_URL = "http://app.nivida.in/bita_fundit/"; //Online URL
    //public static final String BASE_URL = "http://192.168.1.110/fundit/"; // Local URL
    public static final String FILE_URL = BASE_URL + "files/";


    public static final String USER_LOGIN = "User/app_login";
    public static final String COUNTRY_LIST = "Country/app_get_country";
    public static final String STATE_LIST = "State/app_get_state";
    public static final String CITY_LIST = "City/app_get_city";
    public static final String SCHOOL_TYPE = "Type/app_get_type";
    public static final String SCHOOL_SUBTYPE = "SubType/app_get_subtype";
    public static final String CATEGORY_LIST = "Category/app_get_category";

    public static final String UNIQUE_EMAIL = "User/app_check_unique_email";
    public static final String REGISTER_USER = "User/app_register";
    public static final String FORGET_PASSWORD = "User/app_forgot_password";
    public static final String USER_VERIFICATION = "User/app_verify";

    public static final String EDIT_ORGANIZATION_PROFILE = "Organization/app_edit_organization_profile";
    public static final String EDIT_FUNDSPOT_PROFILE = "Fundspot/app_edit_fundspot_profile";
    public static final String EDIT_GENERALMEMBER = "Member/app_edit_member_profile";

    public static final String ADD_PRODUCT = "Product/app_add_product";
    public static final String LIST_PRODUCT = "Product/app_get_product";
    public static final String DELETE_PRODUCT = "Product/app_delete_product";
    public static final String SEARCH_PRODUCT = "Product/app_search_product";
    public static final String EDIT_PRODUCT = "Product/app_edit_product";

    public static final String BROWSE_FUNDSPOT = "Fundspot/app_get_fundspot";
    public static final String BROWSE_ORGANIZATION = "Organization/app_search_organization";
    public static final String FUNDSPOT_PRODUCT = "Product/app_search_product_via_organization";
    public static final String FUNDSPOT_SEARCH = "Fundspot/app_search_fundspot";
    public static final String FUNDSPOT_SEARCH_FUNDSPOT = "Fundspot/app_search_fundspot";

    public static final String ADD_CAMPAIGN = "Campaign/app_add_campaign";

    public static final String ALL_ORGANIZATION = "Organization/app_get_all_organization";
    public static final String ALL_FUNDSPOT = "Fundspot/app_get_all_fundspot";

    public static final String CAMPAIGN_LIST = "Campaign/app_get_all_campaign";

    public static final String MEMBER_LIST = "Member/app_get_member";
    public static final String SEARCHMEMBER_LIST = "member/app_search_member";

    public static final String APP_EDIT_CAMPAIGN = "Campaign/app_edit_campaign";

    public static final String CANCEL_CAMPAIGN = "Campaign/app_action_on_campaign";

    public static final String ForgetPass = "User/app_forgot_password";
    public static final String ForgetPass_OTP = "User/app_otp_verification_for_forgotpass";
    public static final String ForgetPass_edit_change_password = "User/app_change_password";

    public static final String MemberRequest = "Member/app_get_member_request";

    public static final String GetProductsFundspotProducts = "Fundspot/app_get_fundspot_and_products";

    public static final String GetNotificationCount = "Notification/App_Get_UnRead_Notification_Count";

    public static final String GET_ALL_APPROVED_CAMPAIGN = "Campaign/app_get_all_approved_campaign";

    public static final String GET_ALL_NEWS="NewsFeed/app_get_news_feed";

    public static final String GET_ADDRESS="Fundspot/app_get_fundspot_address";

    public static final String GET_ALLFUNDSPOT="Fundspot/app_get_single_fundspot";

    public static final String GET_ALLORGANIZATION="Organization/app_get_single_organization";

    public static final String GET_SELLER_CAMPAIGN = "Campaign/app_get_seller_campaign";

    public static final String GET_MEMBER_FOR_CAMPAIGN = "Campaign/app_get_member_for_campaign";
    public static final String DELETE_MEMBER_FROM_CAMPAIGN = "Campaign/app_delete_member_for_campaign";
    public static final String ADD_MEMBER_TO_CAMPAIGN = "Campaign/app_add_member_to_campaign";
    public static final String VIEW_MEMBER_PROFILE = "Member/app_view_member_profile";


    public static final String ADDORDER = "Order/app_add_order";

    public static final String ORDER_HISTORY = "Order/app_get_order";

    public static final String GET_CARD_DATA = "BankCard/app_get_cards";


    public static final String GET_ALL_INBOX = "Inbox/App_Get_Inbox_Msg";
    public static final String READ_INBOX_MESSAGES = "Inbox/App_Read_Inbox_Notification";
    public static final String SEND_MESSAGES = "Inbox/App_Send_MSG";
    public static final String GET_UNREAD_INBOX_COUNT = "Inbox/App_Get_UnRead_Notification_Count";

    public static final String GET_ALL_MEMBERS = "Member/app_get_member";


    public static final String GET_ALL_ORG = "Organization/app_browse_organization";
    public static final String GET_ALL_FUND = "Fundspot/app_browse_fundspot";
    public static final String MESSGESUSERLIST = "Member/app_get_general_member";
    public  static final String PEOPLE_SEARCH="Member/app_get_general_member";

    public  static final String GET_ORGANIZATION_BROWSE="Organization/app_search_organization";
    public static final String CHECK_JOIN_MEMBER = "Member/App_Check_Join_request";

    public static final String COUPON_REQUEST = "Order/app_accept_coupon_request";
    public static final String LEAVE_MEMBER = "Member/app_delete_member_for_account";
    public static final String GETALLSETTINGSLIST = "NotificationSetting/app_get_notification_setting";
    public static final String UPDATESETTINGS = "NotificationSetting/app_update_notification_setting";





}
