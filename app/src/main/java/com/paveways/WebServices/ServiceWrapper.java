package com.paveways.WebServices;

import androidx.viewbinding.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paveways.Home.GetbannerModel;
import com.paveways.Utility.Constant;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebResponse.ListingsResponse;
import com.paveways.WebResponse.NewUserRegistration;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebResponse.SubCategoriesResponse;
import com.paveways.WebResponse.UserSignInRes;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ServiceWrapper  {

    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return  plainString;
    }


    public Call<NewUserRegistration> newUserRegistrationCall(String fname, String lname, String fullname, String email, String phone, String username, String password){
        return mServiceInterface.NewUserRegistrationCall(convertPlainString(fname),convertPlainString(lname), convertPlainString(fullname),convertPlainString(email), convertPlainString(phone), convertPlainString( username),
                convertPlainString(password));
    }
    ///  user signin
    public Call<UserSignInRes> UserSigninCall(String phone, String password){
        return mServiceInterface.UserSigninCall(convertPlainString(phone),  convertPlainString(password));
    }
    // get banner image
    public Call<GetbannerModel> getbannerModelCall(String securcode){
        return mServiceInterface.getbannerimagecall(convertPlainString(securcode) );
    }

    // get categories
    public Call<CategoriesResponse> CategoriesResponseCall(String securcode){
        return mServiceInterface.CategoriesResponsecall(convertPlainString(securcode) );
    }

    // get sub categories
    public Call<SubCategoriesResponse> SubCategoriesResponseCall(String securcode, String categories_id){
        return mServiceInterface.SubCategoriesResponsecall(convertPlainString(securcode) , convertPlainString(categories_id));
    }

    // get listing
    public Call<ListingsResponse> ListingsResponseCall(String securcode, String sub_categories_id){
        return mServiceInterface.ListingsResponseCall(convertPlainString(securcode) , convertPlainString(sub_categories_id));
    }

    // get product detials
    public Call<ProductDetail_Res> getProductDetails(String securcode, String prod_id){
        return mServiceInterface.getProductDetials(convertPlainString(securcode), convertPlainString(prod_id));
    }


}


