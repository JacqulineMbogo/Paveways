package com.paveways.Listings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Appointment.Appointment_History;
import com.paveways.Auth.LogIn;
import com.paveways.Feedback.FeedbackHistory;
import com.paveways.MyAccount.OrderHistory;
import com.paveways.R;
import com.paveways.TermsAndConditions;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.ListingsResponse;
import com.paveways.WebServices.ServiceWrapper;
import com.paveways.cart.CartDetails;
import com.paveways.cart.Order_Summary;
import com.paveways.cart.PlaceOrderActivity;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listings_Activity extends AppCompatActivity {

    Context context;
    private static String TAG = "productDetails", category_id;

    TextView name;
    LinearLayout linear_for;
    TextView sale, rent;

    private ArrayList<Sub_Categories_Model> Sub_CategoriesModelList = new ArrayList<>();
    private Listings_Adapter sub_categries_adapter;

    private static ArrayList<Listing_Details_Model> ListingModelList = new ArrayList<>();
    private static Listing_Details_Adapter listingDetails_adapter;

    RecyclerView recyclerView, listing_recyclerview;
    SharedPreferenceActivity sharedPreferenceActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.name);
        linear_for = findViewById(R.id.linear_for);
        rent = findViewById(R.id.rent);
        sale = findViewById(R.id.sale);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(this);
        final Intent intent = getIntent();
        category_id = intent.getExtras().getString("category_id");

        listing_recyclerview = findViewById(R.id.recycler_listing);

        LinearLayoutManager mLayoutManger7 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        sub_categries_adapter = new Listings_Adapter(this, Sub_CategoriesModelList, GetScreenWidth());


        GridLayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 2); // you can change grid columns to 3 or more
        listingDetails_adapter = new Listing_Details_Adapter(this, ListingModelList, GetScreenWidth());

        listing_recyclerview.setAdapter(listingDetails_adapter);
        listing_recyclerview.setLayoutManager(mLayoutManger);
        listing_recyclerview.setItemAnimator(new DefaultItemAnimator());

        ArrayList<String> spinnerData = new ArrayList<>();
        if (Objects.equals(category_id, "10002")) {
            ListingModelList.clear();
            listingDetails_adapter.clear();
            // ArrayList<String> containing the data for the Spinner
            name.setText("Houses");
            spinnerData.add("all");
            spinnerData.add("house");
            spinnerData.add("apartment");
            spinnerData.add("office");
            // getProperty("house", context);
        } else {
            ListingModelList.clear();
            listingDetails_adapter.clear();
            linear_for.setVisibility(View.GONE);
            name.setText("Land");
            //spinnerData.add("land");
            getProperty("land", context,"");
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        if (Objects.equals(category_id, "10001")) {
            spinner.setVisibility(View.GONE);
        }
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = adapterView.getItemAtPosition(i).toString();
                ListingModelList.clear();
                listingDetails_adapter.clear();
                sharedPreferenceActivity.putItem(Constant.SELECTED_CATEGORY,selectedOption);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getProperty(sharedPreferenceActivity.getItem(Constant.SELECTED_CATEGORY), context,"rent");


            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getProperty(sharedPreferenceActivity.getItem(Constant.SELECTED_CATEGORY), context,"sale");


            }
        });
    }

    public static void getProperty(String selectedOption, Context context, String stype) {
        ListingModelList.clear();
        ServiceWrapper service = new ServiceWrapper(null);
        Call<ListingsResponse> call = service.ListingsResponseCall("1234", selectedOption,stype);
        call.enqueue(new Callback<ListingsResponse>() {
            @Override
            public void onResponse(Call<ListingsResponse> call, Response<ListingsResponse> response) {


                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        if (response.body().getInformation().size() > 0) {
                            for (int i = 0; i < response.body().getInformation().size(); i++) {
                                Log.d(TAG, "size : " + response.body().getInformation().size());

                                ListingModelList.add(new Listing_Details_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(), response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getStock()));

                            }

                            listingDetails_adapter.notifyDataSetChanged();
                        } else {
                            ListingModelList.clear();
                            listingDetails_adapter.clear();
                            Toast.makeText(context, "No Property found. ", Toast.LENGTH_LONG).show();
                        }


                    } else {

                        ListingModelList.clear();
                        listingDetails_adapter.clear();
                        Toast.makeText(context, "No Property found. ", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ListingsResponse> call, Throwable t) {

            }

        });


    }


    public int GetScreenWidth() {
        int width = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent = new Intent(Listings_Activity.this, CartDetails.class);
                startActivity(intent);
                return true;
            case R.id.listings:
                Intent intent1 = new Intent(Listings_Activity.this, OrderHistory.class);
                intent1.putExtra("securecode", "1");
                startActivity(intent1);
                return true;
            case R.id.booking:
                Intent intent2 = new Intent(Listings_Activity.this, Appointment_History.class);
                startActivity(intent2);
                return true;
            case R.id.feedback:
                Intent intent3 = new Intent(Listings_Activity.this, FeedbackHistory.class);
                startActivity(intent3);
                return true;
            case R.id.signout:
                Intent intent4 = new Intent(Listings_Activity.this, LogIn.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
