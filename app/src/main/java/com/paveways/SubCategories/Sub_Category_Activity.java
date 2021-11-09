package com.paveways.SubCategories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Home.Categories_Adapter;
import com.paveways.Home.Categories_Model;
import com.paveways.Home.HomeActivity;
import com.paveways.Listings.Listings_Adapter;
import com.paveways.Listings.Listings_Model;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.NetworkUtility;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebResponse.ListingsResponse;
import com.paveways.WebResponse.SubCategoriesResponse;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sub_Category_Activity extends AppCompatActivity {

    Context context;
    private static String TAG ="productDetails";
    private String prod_id="";
    private String prod_name="";

    TextView name;

    private ArrayList<Sub_Categories_Model> Sub_CategoriesModelList = new ArrayList<>();
    private Sub_Categories_Adapter sub_categries_adapter;

    private static ArrayList<Listings_Model> ListingModelList = new ArrayList<>();
    private static Listings_Adapter listings_adapter;

    RecyclerView recyclerView, listing_recyclerview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_category);

        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");
        prod_name =  intent.getExtras().getString("prod_name");

        name = findViewById(R.id.name);

        context = this;


        name.setText(prod_name);

        recyclerView = findViewById(R.id.recycler_sub_categories);
        listing_recyclerview = findViewById(R.id.recycler_listing);

        LinearLayoutManager mLayoutManger7 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        sub_categries_adapter=new Sub_Categories_Adapter(this,Sub_CategoriesModelList, GetScreenWidth());


        recyclerView.setAdapter(sub_categries_adapter);
        recyclerView.setLayoutManager(mLayoutManger7);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        GridLayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(),2); // you can change grid columns to 3 or more
        listings_adapter=new Listings_Adapter(this,ListingModelList, GetScreenWidth());

        listing_recyclerview.setAdapter(listings_adapter);
        listing_recyclerview.setLayoutManager(mLayoutManger);
        listing_recyclerview.setItemAnimator(new DefaultItemAnimator());


        getsubcategories();
    }

    private void getsubcategories() {

        if (!NetworkUtility.isNetworkConnected(Sub_Category_Activity.this)){
            AppUtilits.displayMessage(Sub_Category_Activity.this,  getString(R.string.network_not_connected));


        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<SubCategoriesResponse> call = service.SubCategoriesResponseCall("1234", prod_id);
            call.enqueue(new Callback<SubCategoriesResponse>() {
                @Override
                public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {

                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {
                            if (response.body().getInformation().size() > 0) {

                                Sub_CategoriesModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Sub_CategoriesModelList.add(new Sub_Categories_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName()));

                                }

                                sub_categries_adapter.notifyDataSetChanged();
                            }


                        }else {

                            Toast.makeText(getApplicationContext(),"No Sub Categories found",Toast.LENGTH_LONG).show();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<SubCategoriesResponse> call, Throwable t) {

                }

            });
        }
    }


    public static void getsubcategoryproduct(String subcatId) {


            ServiceWrapper service = new ServiceWrapper(null);
            Call<ListingsResponse> call = service.ListingsResponseCall("1234",subcatId);
            call.enqueue(new Callback<ListingsResponse>() {
                @Override
                public void onResponse(Call<ListingsResponse> call, Response<ListingsResponse> response) {
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {
                            if (response.body().getInformation().size() > 0) {

                                ListingModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    Log.e(TAG, "Listing response is "+ response.body().getInformation().get(i).getId());

                                    ListingModelList.add(new Listings_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                }

                                listings_adapter.notifyDataSetChanged();
                            }


                        }else {

                            //Toast.makeText(mContext,"No Categories found",Toast.LENGTH_LONG).show();
                        }


                    }else {
                        //Toast.makeText(Sub_Category_Activity.this,"Something went wrong",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ListingsResponse> call, Throwable t) {

                }

            });




    }


    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


}
