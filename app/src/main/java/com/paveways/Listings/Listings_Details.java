package com.paveways.Listings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.NetworkUtility;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listings_Details extends AppCompatActivity {
    private String TAG ="listingDetails";
    private String prod_id="";

    private TextView prod_name, prod_price, prod_oldprice, prod_rating_count, prod_stock, prod_qty,details;
    private ImageView add_to_cart,view_cart,imageurl,add_to_cart1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_details);



        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");


        details = (TextView) findViewById(R.id.detail);
        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_price =(TextView) findViewById(R.id.prod_price);
        prod_stock = (TextView) findViewById(R.id.stock_avail);
        //prod_qty = (TextView) findViewById(R.id.prod_qty_value);
        imageurl=findViewById(R.id.imgurl);

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
                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getName()!=null){
                                prod_name.setText(response.body().getInformation().getName());


                                    prod_price.setText("Ksh "+""+response.body().getInformation().getPrice());
                                details.setText(response.body().getInformation().getDescription());
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
}
