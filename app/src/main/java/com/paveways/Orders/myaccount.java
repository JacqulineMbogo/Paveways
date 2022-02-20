package com.paveways.Orders;


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

import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.Utility.Constant;
import com.paveways.Utility.SharedPreferenceActivity;


public class myaccount extends AppCompatActivity {

    public static Menu mainmenu;
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private String TAG ="myaccount";
    private TextView myacc_username, myacc_email, myacc_phone, myacc_address, myacc_orderhistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        myacc_username = (TextView) findViewById(R.id.myacc_username);
        myacc_email = (TextView) findViewById(R.id.myacc_email);
        myacc_phone =(TextView) findViewById(R.id.myacc_phone);
        myacc_address = (TextView) findViewById(R.id.myacc_address);
        //  myacc_order =(TextView) findViewById(R.id.myacc_orderhistory);
        myacc_orderhistory = (TextView) findViewById(R.id.myacc_orderhistory);

        myacc_username.setText(sharedPreferenceActivity.getItem(Constant.USER_name));
        myacc_email.setText(sharedPreferenceActivity.getItem(Constant.USER_email));
        myacc_phone.setText(sharedPreferenceActivity.getItem(Constant.USER_phone));


        myacc_orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myaccount.this, OrderHistory.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent1 = new Intent(myaccount.this, HomeActivity.class);

        startActivity(intent1);

    }

}
