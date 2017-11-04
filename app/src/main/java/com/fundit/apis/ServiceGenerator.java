package com.fundit.apis;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fundit.a.W;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.File;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class ServiceGenerator {

    public static AdminAPI getAPIClass(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(W.BASE_URL)
                .client(getRequestHeader())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(AdminAPI.class);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        File file = new File(fileUri);
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        //Log.e("mimeType",mimeType);
        //create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        //MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private static OkHttpClient getRequestHeader() {

        OkHttpClient httpClient = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        return httpClient;
    }
}
