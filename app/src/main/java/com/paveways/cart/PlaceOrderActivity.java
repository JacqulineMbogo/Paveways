package com.paveways.cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paveways.Listings.Listings_Activity;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.PlaceOrder;
import com.paveways.WebServices.ServiceWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.valueOf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlaceOrderActivity extends AppCompatActivity {
    private String TAG =" PlaceOrderActivity";
    private String pin="0", addressid ="0", delivermode ="instant_pay";
    private TextView place_order,  pay, totalpricetxt, shipping,TOTAL;
    private RadioButton radio_eazybanking, radio_cash_on;
    private RelativeLayout relative_lay1, relative_lay2, relative_lay3;
    public String plus="0";
    private RadioGroup radioGroup;

    private Boolean gotoHomeflag = false;
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        addressid =  "0";
        pin= sharedPreferenceActivity.getItem(Constant.USER_Fee);



        radio_cash_on = (RadioButton) findViewById(R.id.radio_cash_on);
        radio_eazybanking = (RadioButton) findViewById(R.id.radio_eazybank);
        radioGroup = findViewById(R.id.radiogroup);

        totalpricetxt = (TextView) findViewById(R.id.totalpricetxt);
        shipping = (TextView) findViewById(R.id.shipping);
        TOTAL =(TextView) findViewById(R.id.TOTAL);
        pay =  findViewById(R.id.pay);

        relative_lay1 = (RelativeLayout) findViewById(R.id.relative_lay1);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);
        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);


        place_order = (TextView) findViewById(R.id.place_order);
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_cash_on.isChecked() || radio_eazybanking.isChecked()) {
                    if (radio_cash_on.isChecked()) {

                        delivermode = "M_Pesa";

                        CallPlaceOrderAPI("0", "0");

                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(PlaceOrderActivity.this, payment2.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);

                                finish();

                            }
                        });
                    } else if (radio_eazybanking.isChecked()) {

                        delivermode = "Eazy banking";

                        CallPlaceOrderAPI("0", "0");

                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(PlaceOrderActivity.this, payment2.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);

                                finish();

                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please select a payment method.",Toast.LENGTH_LONG).show();

                }



            }
        });






    }

    public void CallPlaceOrderAPI( final String addressid, final String pin){

        final AlertDialog progressbar = AppUtilits.createProgressBar(context,"Processing...");
        if (!NetworkUtility.isNetworkConnected(PlaceOrderActivity.this)){
            AppUtilits.displayMessage(PlaceOrderActivity.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);

            Call<PlaceOrder> call = serviceWrapper.placceOrdercall("1234", sharedPreferenceActivity.getItem(Constant.USER_DATA),
                    addressid, sharedPreferenceActivity.getItem(Constant.USER_Totalprice), sharedPreferenceActivity.getItem(Constant.QUOTE_ID), delivermode );

            call.enqueue(new Callback<PlaceOrder>() {
                @Override
                public void onResponse(Call<PlaceOrder> call, Response<PlaceOrder> response) {
                    Log.e(TAG, "place order is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.d("amount", String.valueOf(Constant.USER_Totalprice));
                    Log.d("address", addressid);

                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);

                            gotoHomeflag = true;
                            relative_lay1.setVisibility(View.GONE);
                            relative_lay3.setVisibility(View.GONE);
                            relative_lay2.setVisibility(View.VISIBLE);

                            sharedPreferenceActivity.putItem(Constant.USER_order_id, response.body().getInformation().getOrderId());

                            totalpricetxt.setText("Subtotal : " + "Ksh " +sharedPreferenceActivity.getItem(Constant.USER_SubTotalprice));

                            int plus = Integer.parseInt(sharedPreferenceActivity.getItem(Constant.USER_Totalprice));


                            TOTAL.setText("TOTAL : " + "Ksh " + plus);


                            sharedPreferenceActivity.putItem(Constant.QUOTE_ID, "");

                            sharedPreferenceActivity.putItem(Constant.TOTAL_TOTAL,String.valueOf(plus));



                        }else {
                            AppUtilits.displayMessage(PlaceOrderActivity.this, response.body().getMsg() );
                            AppUtilits.destroyDialog(progressbar);


                        }
                    }else {
                        AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.network_error));
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<PlaceOrder> call, Throwable t) {

                    Log.e(TAG, "  fail- get user address "+ t.toString());
                    AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.fail_togetaddress));

                    AppUtilits.destroyDialog(progressbar);
                }
            });


        }


    }

    @Override
    public void onBackPressed() {



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaceOrderActivity.this);
        alertDialog.setTitle("Cancel Request Confirmation!");
        alertDialog.setMessage("By going back, your order will be cancelled,\n\n" +
                "You cannot make any changes upon submitting\n\n" +
                "Would you like to proceed?\n\n");
        alertDialog.setNeutralButton("No, Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(PlaceOrderActivity.this, Listings_Activity.class);

                startActivity(intent1);
                Toast.makeText(getApplicationContext(),"Order Cancelled",Toast.LENGTH_LONG).show();

            }

        }).show();





    }

}
