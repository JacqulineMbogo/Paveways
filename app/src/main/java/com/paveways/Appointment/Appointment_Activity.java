package com.paveways.Appointment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paveways.Home.HomeActivity;
import com.paveways.Listings.Listing_Details;
import com.paveways.Listings.Listings_Activity;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddAppointment;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebServices.ServiceWrapper;
import com.paveways.cart.CartDetails;
import com.paveways.cart.Order_Summary;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Appointment_Activity extends AppCompatActivity implements
        View.OnClickListener {
    String TAG = "Appointment Activity";
    private String listing_id="",appointment_id="" ;

    SharedPreferenceActivity sharedPreferenceActivity;

    Button btnDatePicker, btnTimePicker,finish;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        final Intent intent = getIntent();
        listing_id =  intent.getExtras().getString("listing_id");
        appointment_id =  intent.getExtras().getString("appointment_id");

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        finish=(Button)findViewById(R.id.finish);
        txtDate=(EditText)findViewById(R.id.date);
        txtTime=(EditText)findViewById(R.id.time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        finish.setOnClickListener(this);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            // Set the minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay >= 6 && hourOfDay < 18) {

                                String timeSuffix = "AM"; // Assume AM by default

                                if (hourOfDay >= 12) {
                                    timeSuffix = "PM";
                                    if (hourOfDay > 12) {
                                        hourOfDay -= 12; // Convert to 12-hour format
                                    }
                                }

                                String formattedMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);

                                txtTime.setText(hourOfDay + ":" + formattedMinute + " " + timeSuffix);
                            } else {
                                // Display an error message
                                AlertDialog.Builder builder = new AlertDialog.Builder(Appointment_Activity.this);
                                builder.setTitle("Invalid Time");
                                builder.setMessage("Please select a time between 6am and 6pm.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();

        }

        if (v == finish) {
            if (TextUtils.isEmpty(txtDate.getText().toString().trim())) {
                AppUtilits.displayMessage(Appointment_Activity.this, "Invalid date. ");
            }else if (TextUtils.isEmpty(txtTime.getText().toString().trim())) {
                AppUtilits.displayMessage(Appointment_Activity.this, "Invalid time. ");
            }else {
                if(appointment_id.equals("0")){
                    bookAppointment("123456");
                }else {

                    bookAppointment("00");
                }

            }
        }

}
    public void bookAppointment(String securecode){

        final AlertDialog progressbar = AppUtilits.createProgressBar(this,"Booking appointment \n Please wait..");

        if (!NetworkUtility.isNetworkConnected(Appointment_Activity.this)){
            AppUtilits.displayMessage(Appointment_Activity.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddAppointment> call = service.addAppointmentCall(securecode, listing_id,sharedPreferenceActivity.getItem(Constant.USER_DATA),txtDate.getText().toString(),txtTime.getText().toString(),appointment_id ,"" );
            call.enqueue(new Callback<AddAppointment>() {
                @Override
                public void onResponse(Call<AddAppointment> call, Response<AddAppointment> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "Appointment response is "+ response.body().getMsg());

                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(Appointment_Activity.this, response.body().getMsg());

                            Intent intent = new Intent(Appointment_Activity.this , Appointment_History.class);
                            startActivity(intent);

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(Appointment_Activity.this, "Unable to book appointment please try again  ");
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(Appointment_Activity.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddAppointment> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(Appointment_Activity.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }
    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(Appointment_Activity.this, HomeActivity.class);

        startActivity(intent1);



    }


}
