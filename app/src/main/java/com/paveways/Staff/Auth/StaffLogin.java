package com.paveways.Staff.Auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Listings.Listings_Activity;
import com.paveways.R;
import com.paveways.StaffProfile;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.DataValidation;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.UserSignInRes;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffLogin extends AppCompatActivity {

    private String TAG = "SigninActivity";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    TextView signup;
    private TextView login,staff_signup;
    private EditText user_name, password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        staff_signup = findViewById(R.id.staff_signup);
        signup.setVisibility(View.GONE);

        staff_signup.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( DataValidation.isNotValidFullName(user_name.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid username",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();

                }else {

                    sendUserLoginData();

                }
            }
        });
    }

    public void sendUserLoginData(){
        final AlertDialog progressbar = AppUtilits.createProgressBar(this,"Please Wait");

        if (!NetworkUtility.isNetworkConnected(StaffLogin.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<UserSignInRes> staffUserSigninCall = serviceWrapper.staffUserSigninCall(user_name.getText().toString(), password.getText().toString());
            staffUserSigninCall.enqueue(new Callback<UserSignInRes>() {
                @Override
                public void onResponse(Call<UserSignInRes> call, Response<UserSignInRes> response) {
                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            AppUtilits.destroyDialog(progressbar);
                            // store userdata to share prerference
                            sharedPreferenceActivity.putItem(Constant.USER_DATA, response.body().getInformation().getUserId());
                            sharedPreferenceActivity.putItem(Constant.USER_name, response.body().getInformation().getFullname());
                            sharedPreferenceActivity.putItem(Constant.USER_email, response.body().getInformation().getEmail());
                            sharedPreferenceActivity.putItem(Constant.USER_phone, response.body().getInformation().getPhone());
                            sharedPreferenceActivity.putItem(Constant.DEPARTMENT, response.body().getMsg());

                            AppUtilits.createToaster(StaffLogin.this, "Welcome, "+sharedPreferenceActivity.getItem(Constant.USER_name),Toast.LENGTH_LONG);
                            Intent intent = new Intent(StaffLogin.this, StaffProfile.class);
                            startActivity(intent);
                            //finish();

                        }else  if (response.body().getStatus() ==0){
                            AppUtilits.displayMessage(StaffLogin.this,  response.body().getMsg());
                            AppUtilits.destroyDialog(progressbar);
                        }
                    }else {
                        AppUtilits.displayMessage(StaffLogin.this,  response.body().getMsg());
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);
                    }
                }

                @Override
                public void onFailure(Call<UserSignInRes> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);

                }
            });




        }








    }
}
