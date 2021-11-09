package com.paveways.Auth;

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

import com.paveways.R;
import com.paveways.ScrollingActivity;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.DataValidation;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.NewUserRegistration;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private String TAG ="SignupActivity";
    SharedPreferenceActivity sharedPreferenceActivity;
    EditText username, phone_no, email, password, retype_password,fname, lname;
    TextView create_acc, signin;
    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        signin = findViewById(R.id.signin);
        create_acc = (TextView) findViewById(R.id.create_account);
        username = (EditText) findViewById(R.id.username);
        fname = (EditText) findViewById(R.id.f_name);
        lname= (EditText) findViewById(R.id.l_name);
        phone_no = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        retype_password = (EditText) findViewById(R.id.retype_password);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });



        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidFName(username.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else if (DataValidation.isNotValidLName(fname.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else
                if (DataValidation.isNotValidFullName(lname.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else if ( DataValidation.isValidPhoneNumber(phone_no.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid phone number.",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidemail(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password should be at least 6 characters ",Toast.LENGTH_LONG).show();

                }else if (!password.getText().toString().equals(retype_password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"passwords do not match",Toast.LENGTH_LONG).show();


                }else {
                    // network connection and progroess dialog
                    /// here call retrofit method

                    sendNewRegistrationReq();
                }

            }
        });
    }
    public void sendNewRegistrationReq(){

        final AlertDialog progressbar = AppUtilits.createProgressBar(this,"Please Wait");

        if (!NetworkUtility.isNetworkConnected(this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);


        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewUserRegistration> callNewREgistration=   serviceWrapper.newUserRegistrationCall( fname.getText().toString(), lname.getText().toString(),username.getText().toString(),
                    email.getText().toString(), phone_no.getText().toString(),
                    username.getText().toString(), password.getText().toString() );
            callNewREgistration.enqueue(new Callback<NewUserRegistration>() {
                @Override
                public void onResponse(Call<NewUserRegistration> call, Response<NewUserRegistration> response) {
                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            AppUtilits.destroyDialog(progressbar);
                            // store userdata to share prerference
                            sharedPreferenceActivity.putItem(Constant.USER_DATA, response.body().getInformation().getUserId());
                            sharedPreferenceActivity.putItem(Constant.USER_name, response.body().getInformation().getFullname());
                            sharedPreferenceActivity.putItem(Constant.USER_email, response.body().getInformation().getEmail());
                            sharedPreferenceActivity.putItem(Constant.USER_phone, response.body().getInformation().getPhone());

                            // start home activity
                            //AppUtilits.displayMessage(SignUpActivity.this,  response.body().getMsg());

                            // R1.setVisibility(View.GONE);
                            // R2.setVisibility(View.VISIBLE);
                            AppUtilits.createToaster(SignUp.this, "Welcome, "+sharedPreferenceActivity.getItem(Constant.USER_name)+"\n Please continue to sign in upon admin approval",Toast.LENGTH_LONG);
                            Intent intent = new Intent(SignUp.this, LogIn.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(SignUp.this,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<NewUserRegistration> call, Throwable t) {
                    AppUtilits.destroyDialog(progressbar);
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();


                }
            });
        }


    }
}
