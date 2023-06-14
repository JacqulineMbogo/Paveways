package com.paveways.cart;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.paveways.Home.HomeActivity;
import com.paveways.Listings.Listings_Activity;
import com.paveways.R;
import com.paveways.TermsAndConditions;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.getCartDetails;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartDetails extends AppCompatActivity {

    private String TAG ="cartdetailss";
    private RecyclerView recycler_cartitem;
    private TextView cart_count, continuebtn,homebtn;
    public  TextView cart_totalamt;
    private Cart_Adapter cartAdapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartdetails);


        recycler_cartitem = (RecyclerView) findViewById(R.id.recycler_cartitem);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_totalamt = (TextView) findViewById(R.id.cart_totalamt);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        homebtn = (TextView) findViewById(R.id.homebtn);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recycler_cartitem.setLayoutManager(mLayoutManger3);
        recycler_cartitem.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new Cart_Adapter(this, cartitemModels);
        recycler_cartitem.setAdapter(cartAdapter);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);


            getUserCartDetails();

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUserCartDetails();
                Intent intent = new Intent(CartDetails.this , TermsAndConditions. class);
                startActivity(intent);


            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserCartDetails();

                Intent intent = new Intent(CartDetails.this , HomeActivity.class);
                startActivity(intent);


            }
        });

    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(CartDetails.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();


        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall( "1234" , sharedPreferenceActivity.getItem(Constant.QUOTE_ID),
                    sharedPreferenceActivity.getItem(Constant.USER_DATA));

            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "  ss sixe 3 ");

                            cart_totalamt .setText(  "Ksh " + response.body().getInformation().getTotalprice());
                            cart_count.setText(getString(R.string.you_have)+" "+ String.valueOf(response.body().getInformation().getProdDetails().size()) +" "+
                                    getString(R.string.product_in_cart));

                            Log.e(TAG, " size is  "+ String.valueOf(response.body().getInformation().getProdDetails().size()));
                            sharedPreferenceActivity.putItem( Constant.CART_ITEM_COUNT, response.body().getInformation().getProdDetails().size()  );

                           sharedPreferenceActivity.putItem(Constant.USER_SubTotalprice, response.body().getInformation().getTotalprice());

                            Log.d("YYY", String.valueOf(response.body().getInformation().getTotalprice()));
                            cartitemModels.clear();

                            for (int i=0; i<response.body().getInformation().getProdDetails().size(); i++){


                                cartitemModels.add( new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(),
                                        response.body().getInformation().getProdDetails().get(i).getImgUrl(),response.body().getInformation().getProdDetails().get(i).getMrp(),
                                        response.body().getInformation().getProdDetails().get(i).getPrice(), response.body().getInformation().getProdDetails().get(i).getQty()));

                            }

                            cartAdapter.notifyDataSetChanged();



                        }else {
                            AppUtilits.displayMessage(CartDetails.this, response.body().getMsg() );
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    Toast.makeText(getApplicationContext(),"please try again. Failed to get user cart details ",Toast.LENGTH_LONG).show();

                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
