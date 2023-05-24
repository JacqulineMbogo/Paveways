package com.paveways.Home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.R;
import com.paveways.Listings.Listings_Activity;

import java.util.List;

public class Categories_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Categories_Model> mCategoriesList;
    private String TAG ="categories_adapter";
    private int mScrenwith;

    public Categories_Adapter(Context context, List<Categories_Model> list, int screenwidth ){
        mContext = context;
        mCategoriesList = list;
        mScrenwith =screenwidth;

    }

    private class CategoriesHolder extends RecyclerView.ViewHolder {

        TextView  name;
        CardView cardView;


        public CategoriesHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_categories, parent,false);
        Log.e(TAG, "  view created ");
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Categories_Model model = mCategoriesList.get(position);
        Log.e(TAG, " assign value ");
        ((CategoriesHolder) holder).name.setText(model.getName());


        ((Categories_Adapter.CategoriesHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, Listings_Activity.class);
                intent.putExtra("prod_id", model.getId());
                intent.putExtra("prod_name", model.getName());

                mContext.startActivity(intent);

                //  Log.e(TAG, "  prod_id "+String.valueOf(prod_id));



            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoriesList.size();
    }

}