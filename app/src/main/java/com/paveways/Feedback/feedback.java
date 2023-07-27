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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.paveways.Home.Categories_Model;
import com.paveways.Home.HomeActivity;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebResponse.StaffResponse;
import com.paveways.WebResponse.feedbackAPI;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedback extends AppCompatActivity {

    private String TAG = "feedbackAPI";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;
    TextView submit;
    private final ArrayList<Staff_Model> StaffModelList = new ArrayList<>();
    EditText title, comment;
    private String staff;
    private final String[] staffIds = {"10001", "10002", "10004", "10005", "10006","10007"};
    private final String[] staffDepartments = {"Customer Acquisition", "Finance", "Super Admin", "Real Estate", "Sales Manager", "Lawyer"};


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        submit = findViewById(R.id.send);
        title = findViewById(R.id.title);
        comment = findViewById(R.id.comment);


        getStaff();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, staffDepartments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.feedback_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStaffId = staffIds[position];
                if (selectedStaffId != null && !selectedStaffId.isEmpty()) {
                    // The selectedStaffId is not null and not empty, perform actions with it
                    Log.e(TAG, "Staff is " + selectedStaffId);
                    staff = selectedStaffId;
                } else {
                    // Handle the case where the selectedStaffId is null or empty
                    Toast.makeText(context, "Staff selected is null or empty", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Selected staff ID is null or empty");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (staff != null && !staff.isEmpty()) {

                    if (title != null && !title.getText().toString().isEmpty()) {

                        if (comment != null && !comment.getText().toString().isEmpty()) {

                            submitFeedback(staff);
                        } else {
                            Toast.makeText(context, "Add Comment", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Add title", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Select staff", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void submitFeedback(String Staff) {
        final AlertDialog progressbar = AppUtilits.createProgressBar(this, "Submitting feedback \n Please Wait");
        if (!NetworkUtility.isNetworkConnected(feedback.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();

        } else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<feedbackAPI> feedbackAPICall = serviceWrapper.feedbackcall("1234", String.valueOf(title.getText().toString()),
                    String.valueOf(comment.getText().toString()), String.valueOf(sharedPreferenceActivity.getItem(Constant.USER_DATA)), staff);
            feedbackAPICall.enqueue(new Callback<feedbackAPI>() {
                @Override
                public void onResponse(Call<feedbackAPI> call, Response<feedbackAPI> response) {
                    Log.d(TAG, "comment : " + String.valueOf(comment));
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(feedback.this, response.body().getMsg());

                            Intent intent = new Intent(feedback.this, Chat_Activity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(feedback.this, response.body().getMsg());

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<feedbackAPI> call, Throwable throwable) {

                    Toast.makeText(getApplicationContext(), "Failed to send feedback", Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);
                }
            });

        }

    }

    private void getStaff() {

        if (!NetworkUtility.isNetworkConnected(feedback.this)) {
            AppUtilits.displayMessage(feedback.this, getString(R.string.network_not_connected));


        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<StaffResponse> call = service.StaffResponseCall("1234");
            call.enqueue(new Callback<StaffResponse>() {
                @Override
                public void onResponse(Call<StaffResponse> call, Response<StaffResponse> response) {
                    Log.e(TAG, "Categories response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                StaffModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    StaffModelList.add(new Staff_Model(response.body().getInformation().get(i).getStaffId(), response.body().getInformation().get(i).getDepartment(),""));

                                }
                            }
                        } else {

                            Toast.makeText(getApplicationContext(), "No staff found", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<StaffResponse> call, Throwable t) {

                }

            });
        }
    }


    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(feedback.this, Chat_Activity.class);

        startActivity(intent1);


    }

}
