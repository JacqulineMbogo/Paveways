package com.paveways.Listings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebServices.ServiceWrapper;
import com.paveways.cart.CartDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listings_Details extends AppCompatActivity {
    private String TAG ="listingDetails";
    private String prod_id="";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private TextView prod_name, prod_price, add_to_cart,view_cart,prod_oldprice, prod_rating_count, prod_stock, prod_qty,details;
    private ImageView imageurl,add_to_cart1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_details);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");


        details = (TextView) findViewById(R.id.detail);
        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_price =(TextView) findViewById(R.id.prod_price);
        prod_stock = (TextView) findViewById(R.id.stock_avail);
        //prod_qty = (TextView) findViewById(R.id.prod_qty_value);
        add_to_cart =  findViewById(R.id.add_to_cart);
        //  add_to_cart1 = (ImageView) findViewById(R.id.add_to_cart1);
        view_cart = findViewById(R.id.view_cart);
        imageurl=findViewById(R.id.imgurl);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                if (prod_stock.getText().toString().equals("Out of Stock") ){

                    Toast.makeText(getApplicationContext(),"Sorry,product is out out stock", Toast.LENGTH_LONG).show();
                }else{
                    addtocartapi( );
                }

            }
        });


        view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Listings_Details.this, CartDetails.class);
                startActivity(intent);


            }
        });

        getProductDetails();
    }

    public void getProductDetails(){


        if (!NetworkUtility.isNetworkConnected(Listings_Details.this)){
            AppUtilits.displayMessage(Listings_Details.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
//                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getName()!=null){
                                prod_name.setText(response.body().getInformation().getName());


                                prod_price.setText("Ksh "+""+response.body().getInformation().getPrice());
                                details.setText(response.body().getInformation().getDescription());
                                prod_stock.setText("Units "+""+response.body().getInformation().getStock());
//                                prod_qty.setText("1");
                                // prod image

                                Glide.with(Listings_Details.this)
                                        .load("http://jacqulinembogo.com/paveways/"+ response.body().getInformation().getImgUrl())
                                        .into(imageurl);

                                // Log.e(TAG, "rating count "+
                                Log.e(TAG, "prod_price "+ prod_price);



                            }


                        } else {
                            Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ProductDetail_Res> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });

        }


    }
    public void addtocartapi(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this,"Adding Listing to cart \n Please wait..");

        if (!NetworkUtility.isNetworkConnected(Listings_Details.this)){
            AppUtilits.displayMessage(Listings_Details.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtoCartCall("12345", prod_id,sharedPreferenceActivity.getItem(Constant.USER_DATA)  );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "prod_id "+ prod_id);
                    Log.e(TAG, "prod_price "+ prod_price);
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                            sharedPreferenceActivity.putItem(Constant.QUOTE_ID, response.body().getInformation().getQouteId());
                            Intent intent = new Intent(Listings_Details.this , CartDetails. class);
                            startActivity(intent);
                            sharedPreferenceActivity.putItem( Constant.CART_ITEM_COUNT,   response.body().getInformation().getCartCount());

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(Listings_Details.this, getString(R.string.fail_add_to_cart));
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(Listings_Details.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(Listings_Details.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }
}
