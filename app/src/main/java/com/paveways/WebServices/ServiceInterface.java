package com.paveways.WebServices;

import com.paveways.Home.GetbannerModel;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebResponse.ListingsResponse;
import com.paveways.WebResponse.NewUserRegistration;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebResponse.SubCategoriesResponse;
import com.paveways.WebResponse.UserSignInRes;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

    @Multipart
    @POST("new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
            @Part("fname") RequestBody fname,
            @Part("lname") RequestBody lname,
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    ///  user signin request
    @Multipart
    @POST("user_signin.php")
    Call<UserSignInRes> UserSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    // get banner image
    @Multipart
    @POST("getbanner.php")
    Call<GetbannerModel> getbannerimagecall(
            @Part("securecode") RequestBody securecode
    );

    // get categories
    @Multipart
    @POST("get_categories.php")
    Call<CategoriesResponse> CategoriesResponsecall(
            @Part("securecode") RequestBody securecode
    );

    // get sub categories
    @Multipart
    @POST("get_sub_category.php")
    Call<SubCategoriesResponse> SubCategoriesResponsecall(
            @Part("securecode") RequestBody securecode,
            @Part("category_id") RequestBody category_id
    );

    // get listings
    @Multipart
    @POST("get_listings.php")
    Call<ListingsResponse> ListingsResponseCall(
            @Part("securecode") RequestBody securecode,
            @Part("sub_category_id") RequestBody sub_category_id
    );

    // get product details
    @Multipart
    @POST("getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );

}




