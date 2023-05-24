package com.paveways.Listings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paveways.Appointment.Appointment_Activity;
import com.paveways.Auth.LogIn;
import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebResponse.ProductDetail_Res;
import com.paveways.WebServices.ServiceWrapper;
import com.paveways.cart.CartDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listing_Details extends AppCompatActivity{
    private String TAG ="listingDetails";
    private String prod_id="";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private TextView prod_name, prod_price, book_visit,make_payment, prod_stock,description,type,status,date,bathrooms,bedrooms;
    private ImageView imageurl;

    private double latitude ;  // Sample latitude
    private double longitude;  // Sample longitude
    private Button openMapsButton;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_details);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");


        description =findViewById(R.id.description);
        prod_name =  findViewById(R.id.prod_name);
        prod_price = findViewById(R.id.prod_price);
        prod_stock =  findViewById(R.id.stock_avail);
        book_visit =  findViewById(R.id.book_visit);
        make_payment = findViewById(R.id.make_payment);
        imageurl=findViewById(R.id.imgurl);
        type=findViewById(R.id.type);
        status=findViewById(R.id.status);
        date=findViewById(R.id.date);
        bathrooms=findViewById(R.id.bathrooms);
        bedrooms=findViewById(R.id.bedrooms);

        Log.d(TAG,sharedPreferenceActivity.getItem(Constant.USER_DATA));


        openMapsButton = findViewById(R.id.openMapsButton);
        openMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInGoogleMaps();
            }
        });


        book_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                if (status.getText().toString().equals("Sold Out") || Integer.parseInt(prod_stock.getText().toString()) <= 0 ){

                    Toast.makeText(getApplicationContext(),"Sorry,property is sold out", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(Listing_Details.this, Appointment_Activity.class);
                    intent.putExtra("listing_id", prod_id);
                    intent.putExtra("appointment_id", "0");
                    startActivity(intent);
                }

            }
        });


        make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                if (status.getText().toString().equals("Sold Out") || Integer.parseInt(prod_stock.getText().toString()) <= 0 ){

                    Toast.makeText(getApplicationContext(),"Sorry,property is sold out", Toast.LENGTH_LONG).show();
                }else{
                    addtocartapi();
                }

            }
        });

        getProductDetails();
    }

    public void getProductDetails(){


        if (!NetworkUtility.isNetworkConnected(Listing_Details.this)){
            AppUtilits.displayMessage(Listing_Details.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "Listing response is "+ response.body().getInformation().toString());

                            if (response.body().getInformation().getName()!=null){
                                Glide.with(Listing_Details.this)
                                        .load("http://demkadairy.co.ke/paveways/admin/property/"+ response.body().getInformation().getImgUrl1())
                                        .into(imageurl);

                                String[] parts = response.body().getInformation().getCity().split("\\|");

                                latitude = Double.parseDouble(parts[1]);
                                longitude = Double.parseDouble(parts[2]);
                                Log.e(TAG, "Lat"+ latitude);
                                Log.e(TAG, "Long"+ longitude);

                                description.setText(response.body().getInformation().getDescription());
                                prod_name.setText(response.body().getInformation().getName());
                                prod_price.setText(response.body().getInformation().getPrice());
                                prod_stock.setText(response.body().getInformation().getStock());
                                type.setText(response.body().getInformation().getType());
                                status.setText(response.body().getInformation().getStatus());
                                date.setText(response.body().getInformation().getDate());
                                bathrooms.setText(response.body().getInformation().getBathroom());
                                bedrooms.setText(response.body().getInformation().getBedroom());

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

        final AlertDialog progressbar =AppUtilits.createProgressBar(this,"Adding to cart");

        if (!NetworkUtility.isNetworkConnected(Listing_Details.this)){
            AppUtilits.displayMessage(Listing_Details.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtoCartCall("12345", prod_id,sharedPreferenceActivity.getItem(Constant.USER_DATA)  );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "prod_id "+ prod_id);

                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "quote id "+ response.body().getInformation().getQouteId());
                            AppUtilits.destroyDialog(progressbar);
                            sharedPreferenceActivity.putItem(Constant.QUOTE_ID, response.body().getInformation().getQouteId());
                            Intent intent = new Intent(Listing_Details.this , CartDetails. class);
                            startActivity(intent);
                            sharedPreferenceActivity.putItem( Constant.CART_ITEM_COUNT,   response.body().getInformation().getCartCount());

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(Listing_Details.this, getString(R.string.fail_add_to_cart));
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(Listing_Details.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(Listing_Details.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }
    private void openInGoogleMaps() {
        String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }


}
