package com.paveways.WebServices;

import com.paveways.Home.GetbannerModel;
import com.paveways.WebResponse.AboutResponse;
import com.paveways.WebResponse.AddAppointment;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebResponse.AppointmentHistoryAPI;
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
import com.paveways.WebResponse.UserDetails;
import com.paveways.WebResponse.UserSignInRes;
import com.paveways.WebResponse.clearbalanceAPI;
import com.paveways.WebResponse.codeAPI;
import com.paveways.WebResponse.feedbackAPI;
import com.paveways.WebResponse.feedhistoryAPI;
import com.paveways.WebResponse.getCartDetails;
import com.paveways.WebResponse.payAPI;
import com.paveways.WebResponse.receiveAPI;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

    @Multipart
    @POST("new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
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
            @Part("user_name") RequestBody phone,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("staff/user_signin.php")
    Call<UserSignInRes> StaffUserSigninCall(
            @Part("user_name") RequestBody phone,
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
            @Part("selectedOption") RequestBody selectedOption,
            @Part("stype") RequestBody stype
             );
    // get product details
    @Multipart
    @POST("getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );
    // get feedback history
    @Multipart
    @POST("getallfeedback.php")
    Call<feedhistoryAPI> getfeedhistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // feedbackAPI
    @Multipart
    @POST("getfeedback.php")
    Call<feedbackAPI> feedbackcall(
            @Part("securecode") RequestBody securecode,
            @Part("feed_title") RequestBody feed_title,
            @Part("feed_comment") RequestBody feed_comment,
            @Part("user_id") RequestBody user_id

    );
    // add to cart
    @Multipart
    @POST("add_prod_into_cart.php")
    Call<AddtoCart> addtocartcall(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id,
            @Part("user_id") RequestBody user_id

    );

    @Multipart
    @POST("add_appointment.php")
    Call<AddAppointment> addAppointmentCall(
            @Part("securecode") RequestBody securecode,
            @Part("listing_id") RequestBody listing_id,
            @Part("user_id") RequestBody user_id,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("appointment_id") RequestBody appointment_id,
            @Part("comment") RequestBody comment

    );

    @Multipart
    @POST("edit_user_details.php")
    Call<AddAppointment> editUserDetailsCall(
            @Part("securecode") RequestBody securecode,
            @Part("staff_id") RequestBody staff_id,
            @Part("user_id") RequestBody user_id

    );

    @Multipart
    @POST("edit_order.php")
    Call<AddAppointment> editOrderDetailsCall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("staff_id") RequestBody staff_id

    );

    // get user cart
    @Multipart
    @POST("getusercartdetails.php")
    Call<getCartDetails> getusercartcall(
            @Part("securecode") RequestBody securecode,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("user_id") RequestBody user_id
    );
    // delete cart item
    @Multipart
    @POST("deletecartitem.php")
    Call<AddtoCart> deleteCartProd(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id
    );

    // edit cart qty
    @Multipart
    @POST("editcartitem.php")
    Call<EditCartItem> editCartQty(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id,
            @Part("prod_qty") RequestBody prod_qty
    );

    // get order summery
    @Multipart
    @POST("getordersummary.php")
    Call<OrderSummary> getOrderSummarycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("qoute_id") RequestBody qoute_id
    );

    // place order api
    @Multipart
    @POST("placeorderapi.php")
    Call<PlaceOrder> PlaceOrderCall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("address_id") RequestBody address_id,
            @Part("total_price") RequestBody total_price,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("deliverymode") RequestBody deliverymode
    );
    // get order history
    @Multipart
    @POST("getorderhistory.php")
    Call<OrderHistoryAPI> getorderHistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    @Multipart
    @POST("getAppointmentHistory.php")
    Call<AppointmentHistoryAPI> getAppointmentHistoryCall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    @Multipart
    @POST("getUsers.php")
    Call<UserDetails> getuserDetailsHistoryCall(
            @Part("securecode") RequestBody securecode );



    @Multipart
    @POST("makepayment.php")
    Call<payAPI> makepaymentcall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
            @Part("payment_amount") RequestBody payment_amount

    );

    // CLEAR BALANCE
    @Multipart
    @POST("clearbalance.php")
    Call<clearbalanceAPI> clearbalancecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
            @Part("code") RequestBody code,
            @Part("mode") RequestBody mode,
            @Part("amount") RequestBody amount

    );

    // RECEIVE
    @Multipart
    @POST("receive.php")
    Call<receiveAPI> receivecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("status") RequestBody status

    );
    // code
    @Multipart
    @POST("code.php")
    Call<codeAPI> codecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("code") RequestBody code

    );
    // get order prodct details history
    @Multipart
    @POST("getorderhistoryproddetails.php")
    Call<GetOrderProductDetails> getorderProductdetailscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id
    );

    @Multipart
    @POST("get_about.php")
    Call<AboutResponse> AboutResponsecall(
            @Part("securecode") RequestBody securecode
    );


}




