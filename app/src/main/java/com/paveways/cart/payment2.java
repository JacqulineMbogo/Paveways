package com.paveways.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.paveways.Auth.LogIn;
import com.paveways.Home.HomeActivity;
import com.paveways.Listings.Listings_Activity;
import com.paveways.MyAccount.OrderHistory;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.DataValidation;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.codeAPI;
import com.paveways.WebResponse.payAPI;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment2 extends AppCompatActivity {
    private String TAG = "payment";

    EditText payment_amount,payment_amount1;
    TextView thanks, request, continuebtn,continuebtn1 ;
    Button home, order,home2;
    RelativeLayout relative_lay2, relative_lay3,extrarelative_lay2;

    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);
        extrarelative_lay2 = (RelativeLayout) findViewById(R.id.extralayout);

        thanks = (TextView) findViewById(R.id.thanks);
        request = (TextView) findViewById(R.id.your_request);
        payment_amount = (EditText) findViewById(R.id.amount);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        payment_amount1 = (EditText) findViewById(R.id.amount1);
        continuebtn1 = (TextView) findViewById(R.id.continuebtn1);
        home=findViewById(R.id.homebutton);
        home2=findViewById(R.id.homebutton2);
        order=findViewById(R.id.shopimage);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

        payment_amount.setText(String.valueOf(sharedPreferenceActivity.getItem(Constant.TOTAL_TOTAL)));


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( DataValidation.isAmountLessThanHalf(Double.parseDouble(payment_amount.getText().toString()), Double.parseDouble(sharedPreferenceActivity.getItem(Constant.TOTAL_TOTAL)))){
                    Toast.makeText(getApplicationContext(),"Payment Amount should not be less than half of the total ",Toast.LENGTH_LONG).show();

                }else {
                    makepayment("1234",payment_amount.getText().toString());

                }

            }


        });
        continuebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( DataValidation.isNotValidcode(payment_amount1.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid code length. Should be 10 characters.",Toast.LENGTH_LONG).show();

                }else {
                    getcode();

                }
            }


        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void makepayment(String securecode,String amount){

        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<payAPI> makepaymentAPICall=serviceWrapper.makepaymentcall(securecode, sharedPreferenceActivity.getItem(Constant.USER_DATA),  sharedPreferenceActivity.getItem(Constant.USER_order_id),
                   String.valueOf(sharedPreferenceActivity.getItem(Constant.TOTAL_TOTAL)), amount);
            makepaymentAPICall.enqueue(new Callback<payAPI>() {
                @Override
                public void onResponse(Call<payAPI> call, Response<payAPI> response) {

                        Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                        //  Log.e(TAG, "  ss sixe 1 ");
                        if (response.body() != null && response.isSuccessful()) {
                            //    Log.e(TAG, "  ss sixe 2 ");
                            if (response.body().getStatus() == 1) {

                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                                extrarelative_lay2.setVisibility(View.VISIBLE);

                                relative_lay2.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();

                            }

                        } else {
                            Toast.makeText(getApplicationContext(),"Something Went wrong",Toast.LENGTH_LONG).show();
                        }



                }

                @Override
                public void onFailure(Call<payAPI> call, Throwable t) {


                    Log.e(TAG, "  fail- to make payment"+ t.toString());
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();


                }
            });

        }


    }

    public void getcode(){
        final AlertDialog progressbar =AppUtilits.createProgressBar(context,"Verifying code");
        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<codeAPI> codeAPICall=serviceWrapper.codecall("1234", sharedPreferenceActivity.getItem(Constant.USER_order_id),
                    String.valueOf(payment_amount1.getText().toString()));
            codeAPICall.enqueue(new Callback<codeAPI>() {
                @Override
                public void onResponse(Call<codeAPI> call, Response<codeAPI> response) {


                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            extrarelative_lay2.setVisibility(View.GONE);

                            relative_lay3.setVisibility(View.VISIBLE);

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(payment2.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(payment2.this, getString(R.string.network_error));

                        AppUtilits.destroyDialog(progressbar);
                    }



                }

                @Override
                public void onFailure(Call<codeAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                    AppUtilits.destroyDialog(progressbar);
                }


            });

        }


    }

    @Override
    public void onBackPressed() {



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(payment2.this);
        alertDialog.setTitle("Cancel Request Confirmation!");
        alertDialog.setMessage(
                "You cannot make any changes upon submitting\n\n" +
                        "Would you like to proceed?\n\n");
        alertDialog.setNeutralButton("No, Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("Yes,Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //makepayment("00","0");
                Intent intent1 = new Intent(payment2.this, HomeActivity.class);

                startActivity(intent1);

            }

        }).show();

    }

}

