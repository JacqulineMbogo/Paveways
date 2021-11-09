package com.paveways.Home;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.NetworkUtility;
import com.paveways.WebResponse.CategoriesResponse;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeActivity extends AppCompatActivity {

    private BannerSlider bannerSlider;
    private List<Banner> remoteBanners=new ArrayList<>();

    private ArrayList<Categories_Model> CategoriesModelList = new ArrayList<>();
    private Categories_Adapter categries_adapter;

    RecyclerView recyclerView;

    String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);

        recyclerView = findViewById(R.id.recycler_categories);
        LinearLayoutManager mLayoutManger7 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categries_adapter=new Categories_Adapter(this,CategoriesModelList, GetScreenWidth());

        recyclerView.setAdapter(categries_adapter);
        recyclerView.setLayoutManager(mLayoutManger7);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getbannerimg();
        getcategories();
    }



    public void getbannerimg(){
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));


        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetbannerModel> call = service.getbannerModelCall("1234");
            call.enqueue(new Callback<GetbannerModel>() {
                @Override
                public void onResponse(Call<GetbannerModel> call, Response<GetbannerModel> response) {
                      Log.e(TAG, " banner response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {
                            if (response.body().getInformation().size() > 0) {

                                 for (int i=0; i<response.body().getInformation().size(); i++) {
                                    remoteBanners.add(new RemoteBanner(response.body().getInformation().get(i).getImgurl()));

                                }


                            }else {

                                remoteBanners.add(new RemoteBanner("https://jacqulinembogo.com/paveways/prod_images/2Bedroom.jpg"));
                                remoteBanners.add(new RemoteBanner("https://jacqulinembogo.com/paveways/prod_images/1.jpeg"));
                            }

                            bannerSlider.setBanners(remoteBanners);
                        }else {
                            remoteBanners.add(new RemoteBanner("https://jacqulinembogo.com/paveways/prod_images/2Bedroom.jpg"));
                            remoteBanners.add(new RemoteBanner("https://jacqulinembogo.com/paveways/prod_images/1.jpeg"));
                            bannerSlider.setBanners(remoteBanners);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetbannerModel> call, Throwable t) {
                    //  Log.e(TAG, "fail banner ads "+ t.toString());
                }
            });
        }

    }

    private void getcategories() {

        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));


        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<CategoriesResponse> call = service.CategoriesResponseCall("1234");
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    Log.e(TAG, "Categories response is "+ response.body().getInformation().toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1) {
                            if (response.body().getInformation().size() > 0) {

                                CategoriesModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    CategoriesModelList.add(new Categories_Model(response.body().getInformation().get(i).getCategoryId(), response.body().getInformation().get(i).getCategoryName()));

                                }

                                categries_adapter.notifyDataSetChanged();
                            }


                            }else {

                                Toast.makeText(getApplicationContext(),"No Categories found",Toast.LENGTH_LONG).show();
                                 }


                        }else {
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                        }
                    }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {

                }

            });
        }
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
