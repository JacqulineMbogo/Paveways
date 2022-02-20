package com.paveways.Feedback;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.feedbackAPI;
import com.paveways.WebServices.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedback  extends AppCompatActivity{

    private String TAG = "feedbackAPI";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;
    TextView submit;
    EditText title, comment;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        submit= findViewById(R.id.send);
       title= findViewById(R.id.title);
       comment=findViewById(R.id.comment);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitfeedback();
            }
        });
    }

    public void submitfeedback(){
        final AlertDialog progressbar = AppUtilits.createProgressBar(this,"Submitting feedback \n Please Wait");
        if (!NetworkUtility.isNetworkConnected(feedback.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<feedbackAPI> feedbackAPICall=serviceWrapper.feedbackcall("1234", String.valueOf(title.getText().toString()),
                  String.valueOf(comment.getText().toString()),String.valueOf(sharedPreferenceActivity.getItem(Constant.USER_DATA)));
            feedbackAPICall.enqueue(new Callback<feedbackAPI>() {
                @Override
                public void onResponse(Call<feedbackAPI> call, Response<feedbackAPI> response) {


                    Log.d(TAG, "reponse : "+ response.toString());
                    Log.d(TAG, "title : "+ String.valueOf(title));
                    Log.d(TAG, "comment : "+ String.valueOf(comment));
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(feedback.this, response.body().getMsg() );

                            Intent intent = new Intent(feedback.this, FeedbackHistory.class);
                            startActivity(intent);
                            finish();

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(feedback.this, response.body().getMsg());

                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<feedbackAPI> call, Throwable throwable) {

                    Toast.makeText(getApplicationContext(),"Failed to send feedback",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);
                }
            });

        }

    }



    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(feedback.this, FeedbackHistory.class);

        startActivity(intent1);



    }

}
