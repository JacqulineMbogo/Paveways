package com.paveways.WebServices;

import androidx.viewbinding.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paveways.Home.GetbannerModel;
import com.paveways.Utility.Constant;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebResponse.EditCartItem;
import com.paveways.WebResponse.GetOrderProductDetails;
import com.paveways.WebResponse.ListingsResponse;
import com.paveways.WebResponse.NewUserRegistration;
import com.paveways.WebResponse.OrderHistoryAPI;
import com.paveways.WebResponse.OrderSummary;
import com.paveways.WebResponse.PlaceOrder;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebResponse.SubCategoriesResponse;
import com.paveways.WebResponse.UserSignInRes;
import com.paveways.WebResponse.clearbalanceAPI;
import com.paveways.WebResponse.codeAPI;
import com.paveways.WebResponse.feedbackAPI;
import com.paveways.WebResponse.feedhistoryAPI;
import com.paveways.WebResponse.getCartDetails;
import com.paveways.WebResponse.payAPI;
import com.paveways.WebResponse.receiveAPI;

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

    // get feedback history
    public Call<feedhistoryAPI> getfeedhistorycall(String securcode, String user_id){
        return mServiceInterface.getfeedhistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }

    public Call<feedbackAPI> feedbackcall(String securcode, String feed_title , String feed_comment, String user_id){
        return mServiceInterface. feedbackcall(convertPlainString(securcode), convertPlainString(feed_title) , convertPlainString(feed_comment), convertPlainString(user_id) );
    }

    public Call<AddtoCart> addtoCartCall(String securcode, String prod_id, String user_id){
        return mServiceInterface.addtocartcall(convertPlainString(securcode), convertPlainString(prod_id),convertPlainString(user_id) );
    }

    // add to cart
    public Call<getCartDetails> getCartDetailsCall(String securcode, String qoute_id, String user_id){
        return mServiceInterface.getusercartcall(convertPlainString(securcode), convertPlainString(qoute_id),convertPlainString(user_id) );
    }
    // delete cart item
    public Call<AddtoCart> deletecartprod(String securcode, String user_id, String prod_id){
        return mServiceInterface.deleteCartProd(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id) );
    }
    // edit cart item
    public Call<EditCartItem> editcartcartprodqty(String securcode, String user_id, String prod_id, String prod_qty){
        return mServiceInterface.editCartQty(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id),  convertPlainString(prod_qty) );
    }

    // get order summery
    public Call<OrderSummary> getOrderSummarycall(String securcode, String user_id, String qoute_id){
        return mServiceInterface.getOrderSummarycall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(qoute_id) );
    }

    // get place order api
    public Call<PlaceOrder> placceOrdercall(String securcode, String user_id, String address_id,
                                            String total_price, String qoute_id, String delivermode){
        return mServiceInterface.PlaceOrderCall(convertPlainString(securcode), convertPlainString(user_id),
                convertPlainString(address_id), convertPlainString(total_price), convertPlainString(qoute_id), convertPlainString( delivermode));
    }

    // get order history
    public Call<OrderHistoryAPI> getorderhistorycall(String securcode, String user_id){
        return mServiceInterface.getorderHistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }

    // get order prodcut detais history
    public Call<payAPI> makepaymentcall(String securcode, String user_id, String order_id , String total_price, String payment_amount){
        return mServiceInterface. makepaymentcall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(payment_amount) );
    }

    public Call<clearbalanceAPI> clearbalancecall(String securcode, String order_id , String total_price, String code, String mode, String amount){
        return mServiceInterface. clearbalancecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(code),  convertPlainString(mode), convertPlainString(amount) );
    }

    public Call<receiveAPI> receivecall(String securcode, String order_id , String status){
        return mServiceInterface. receivecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(status) );
    }
    ///receive code
    public Call<codeAPI> codecall(String securcode, String order_id , String code){
        return mServiceInterface. codecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(code) );
    }
    // get order prodcut detais history
    public Call<GetOrderProductDetails> getorderproductcall(String securcode, String user_id, String order_id){
        return mServiceInterface.getorderProductdetailscall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) );
    }

}


