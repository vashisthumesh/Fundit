package com.fundit.a;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class W {

    public static final String KEY_USERID = "user_id";
    public static final String KEY_TOKEN = "tokenhash";

    public static final String BASE_URL="http://app.nivida.in/fundit/";
    public static final String FILE_URL = BASE_URL + "files/";

    public static final String USER_LOGIN="User/app_login";
    public static final String COUNTRY_LIST="Country/app_get_country";
    public static final String STATE_LIST="State/app_get_state";
    public static final String CITY_LIST="City/app_get_city";
    public static final String SCHOOL_TYPE="Type/app_get_type";
    public static final String SCHOOL_SUBTYPE="SubType/app_get_subtype";
    public static final String CATEGORY_LIST="Category/app_get_category";

    public static final String UNIQUE_EMAIL="User/app_check_unique_email";
    public static final String REGISTER_USER="User/app_register";
    public static final String FORGET_PASSWORD="User/app_forgot_password";
    public static final String USER_VERIFICATION="User/app_verify";

    public static final String EDIT_ORGANIZATION_PROFILE = "Organization/app_edit_organization_profile";
    public static final String EDIT_FUNDSPOT_PROFILE = "Fundspot/app_edit_fundspot_profile";
    public static final String EDIT_GENERALMEMBER = "Member/app_edit_member_profile";

    public static final String ADD_PRODUCT = "Product/app_add_product";
    public static final String LIST_PRODUCT = "Product/app_get_product";
    public static final String DELETE_PRODUCT = "Product/app_delete_product";
    public static final String SEARCH_PRODUCT = "Product/app_search_product";
    public static final String EDIT_PRODUCT = "Product/app_edit_product";

    public static final String BROWSE_FUNDSPOT = "Fundspot/app_get_fundspot";
    public static final String FUNDSPOT_PRODUCT = "Product/app_search_product_via_organization";
    public static final String FUNDSPOT_SEARCH = "Fundspot/app_search_fundspot";

    public static final String ADD_CAMPAIGN = "Campaign/app_add_campaign";

    public static final String ALL_ORGANIZATION="Organization/app_get_all_organization";
    public static final String ALL_FUNDSPOT="Fundspot/app_get_all_fundspot";

    public static final String CAMPAIGN_LIST = "Campaign/app_get_all_campaign";
}
