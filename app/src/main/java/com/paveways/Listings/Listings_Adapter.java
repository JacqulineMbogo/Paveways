package com.paveways.Listings;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paveways.Home.Categories_Adapter;
import com.paveways.Home.Categories_Model;
import com.paveways.R;
import com.paveways.SubCategories.Sub_Category_Activity;

import java.util.List;

public class Listings_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Listings_Model> mListingsList;
    private String TAG ="listings adapter";
    private int mScrenwith;

    public Listings_Adapter(Context context, List<Listings_Model> list, int screenwidth ){
        mContext = context;
        mListingsList = list;
        mScrenwith =screenwidth;

    }

    private class ListingsHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView cardView;
        ImageView prod_image;


        public ListingsHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            prod_image = itemView.findViewById(R.id.prod_img);

            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( mScrenwith - (mScrenwith/100*70), LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(5,5,5,5);
            cardView.setLayoutParams(params);
            cardView.setPadding(5,5,5,5);*/

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listing, parent,false);
        Log.e(TAG, "  view created ");
        return new Listings_Adapter.ListingsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Listings_Model model = mListingsList.get(position);
        Log.e(TAG, model.getImg_ulr());
        ((Listings_Adapter.ListingsHolder) holder).name.setText(model.getL_name());




        Glide.with(mContext)
                .load("http://jacqulinembogo.com/paveways/"+ model.getImg_ulr())
                .into(((ListingsHolder) holder).prod_image);
        // imageview glider lib to get imagge from url


        ((Listings_Adapter.ListingsHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, Listings_Details.class);
                intent.putExtra("prod_id", model.getL_id());


                mContext.startActivity(intent);

                //  Log.e(TAG, "  prod_id "+String.valueOf(prod_id));



            }
        });

    }

    @Override
    public int getItemCount() {
        return mListingsList.size();
    }

}