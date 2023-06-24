package com.paveways;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Auth.LogIn;
import com.paveways.Auth.SignUp;
import com.paveways.MyAccount.OrderHistory;
import com.paveways.Staff.Auth.StaffLogin;
import com.paveways.Staff.StaffAppointment.StaffAppointment_History;
import com.paveways.Users.Users_Home;
import com.paveways.Utility.Constant;
import com.paveways.Utility.SharedPreferenceActivity;

import java.util.Objects;

public class StaffProfile extends AppCompatActivity {
    SharedPreferenceActivity sharedPreferenceActivity;
    private String TAG ="myaccount";
    private TextView myacc_username, myacc_email, myacc_phone,department;
    private LinearLayout users,bookings,orders,payments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        myacc_username = (TextView) findViewById(R.id.myacc_username);
        myacc_email = (TextView) findViewById(R.id.myacc_email);
        myacc_phone =(TextView) findViewById(R.id.myacc_phone);
        department =(TextView) findViewById(R.id.department);
        users = findViewById(R.id.users);
        bookings = findViewById(R.id.bookings);
        orders = findViewById(R.id.orders);
        payments = findViewById(R.id.payments);


        myacc_username.setText(sharedPreferenceActivity.getItem(Constant.USER_name));
        myacc_email.setText(sharedPreferenceActivity.getItem(Constant.USER_email));
        myacc_phone.setText(sharedPreferenceActivity.getItem(Constant.USER_phone));
        department.setText(sharedPreferenceActivity.getItem(Constant.DEPARTMENT) + " Department");

        if(sharedPreferenceActivity.getItem(Constant.DEPARTMENT).equalsIgnoreCase("admin")){
            orders.setVisibility(View.GONE);
            payments.setVisibility(View.GONE);
        }else if(sharedPreferenceActivity.getItem(Constant.DEPARTMENT).equalsIgnoreCase("finance")){
            users.setVisibility(View.GONE);
            bookings.setVisibility(View.GONE);
                 }

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffProfile.this, Users_Home.class);
                startActivity(intent);
                finish();

            }
        });

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffProfile.this, StaffAppointment_History.class);
                startActivity(intent);
                finish();

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffProfile.this, OrderHistory.class);

                intent.putExtra("securecode","00");
                startActivity(intent);
                finish();

            }
        });

    }
    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(StaffProfile.this, StaffLogin.class);

        startActivity(intent1);



    }

}