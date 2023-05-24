package com.paveways.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.paveways.Listings.Listings_Activity;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.OrderSummary;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Summary extends AppCompatActivity {


    private String TAG = "ordersummery";
    private TextView place_order, subtotalvalue, shippingvalue, ordertotalvalue;

    private RecyclerView item_recyclerview;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private OrderSummary_Adapter orderSummeryAdapter;
    private float totalamount =0;
    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);


        place_order = (TextView) findViewById(R.id.place_order);
        subtotalvalue = (TextView) findViewById(R.id.subtotal_value);
        shippingvalue = (TextView) findViewById(R.id.shipping_value);
        ordertotalvalue = (TextView) findViewById(R.id.order_total_value);

        item_recyclerview = (RecyclerView) findViewById(R.id.item_recyclerview);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mLayoutManger3);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        orderSummeryAdapter = new OrderSummary_Adapter(this, cartitemModels);
        item_recyclerview.setAdapter(orderSummeryAdapter);

        getUserCartDetails();

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  total amount is "+String.valueOf(totalamount));
                if ( totalamount >0){

                    Intent intent = new Intent(Order_Summary.this, PlaceOrderActivity.class);
                    intent.putExtra("amount",  String.valueOf(totalamount));
                    startActivity(intent);

                }


            }
        });

    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(Order_Summary.this)){
            AppUtilits.displayMessage(Order_Summary.this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<OrderSummary> call = service.getOrderSummarycall("1234" , sharedPreferenceActivity.getItem(Constant.USER_DATA),
                    sharedPreferenceActivity.getItem(Constant.QUOTE_ID));

            call.enqueue(new Callback<OrderSummary>() {
                @Override
                public void onResponse(Call<OrderSummary> call, Response<OrderSummary> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            subtotalvalue.setText("Ksh "+response.body().getInformation().getSubtotal());
                            shippingvalue.setText(response.body().getInformation().getShippingfee());
                            ordertotalvalue.setText("Ksh "+response.body().getInformation().getOrdertotal());
                            try {

                                totalamount = Float.valueOf( response.body().getInformation().getOrdertotal());

                            }catch (Exception e){
                                Log.e(TAG, "amount error "+ e.toString() );
                            }
                            cartitemModels.clear();
                            for (int i=0; i<response.body().getInformation().getProdDetails().size(); i++){


                                cartitemModels.add( new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(), "", "",
                                        response.body().getInformation().getProdDetails().get(i).getPrice(), response.body().getInformation().getProdDetails().get(i).getQty()));

                            }

                            orderSummeryAdapter.notifyDataSetChanged();


                        } else {
                            AppUtilits.displayMessage(Order_Summary.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.displayMessage(Order_Summary.this, getString(R.string.network_error));
                    }

                }

                @Override
                public void onFailure(Call<OrderSummary> call, Throwable t) {

                    Log.e(TAG, "  fail- order symmer item "+ t.toString());
                    AppUtilits.displayMessage(Order_Summary.this, getString(R.string.fail_toordersummery));


                }
            });



        }






    }


}


