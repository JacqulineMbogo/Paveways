package com.paveways.Listings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.paveways.R;

import java.util.List;

public class Listings_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Sub_Categories_Model> mSubCategoriesList;
    private String TAG ="sub_categories_adapter";
    private int mScrenwith;

    public Listings_Adapter(Context context, List<Sub_Categories_Model> list, int screenwidth ){
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
        return new Listings_Adapter.SubCategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Sub_Categories_Model model = mSubCategoriesList.get(position);
        Log.e(TAG, " assign value ");

        ((Listings_Adapter.SubCategoriesHolder) holder).name.setText(model.getName());

        ((SubCategoriesHolder) holder).name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Sub_Category_Activity.getsubcategoryproduct(model.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubCategoriesList.size();
    }

}