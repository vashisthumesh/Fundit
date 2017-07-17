package com.fundit.apis;

import com.fundit.a.W;
import com.fundit.model.AppModel;
import com.fundit.model.AreaResponse;
import com.fundit.model.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    Call<AppModel> checkUniqueMail(@Field("email_id") String emailID);




}
