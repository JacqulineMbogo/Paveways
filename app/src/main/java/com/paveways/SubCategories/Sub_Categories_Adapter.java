package com.paveways.SubCategories;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sub_Categories_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Sub_Categories_Model> mSubCategoriesList;
    private String TAG ="sub_categories_adapter";
    private int mScrenwith;

    public Sub_Categories_Adapter(Context context, List<Sub_Categories_Model> list, int screenwidth ){
        mContext = context;
        mSubCategoriesList = list;
        mScrenwith =screenwidth;
    }

    private class SubCategoriesHolder extends RecyclerView.ViewHolder {
        Button name;
        RecyclerView recyclerView;

        public SubCategoriesHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sub_categories, parent,false);
        Log.e(TAG, "  view created ");
        return new Sub_Categories_Adapter.SubCategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Sub_Categories_Model model = mSubCategoriesList.get(position);
        Log.e(TAG, " assign value ");

        ((Sub_Categories_Adapter.SubCategoriesHolder) holder).name.setText(model.getName());

        ((SubCategoriesHolder) holder).name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sub_Category_Activity.getsubcategoryproduct(model.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubCategoriesList.size();
    }

}