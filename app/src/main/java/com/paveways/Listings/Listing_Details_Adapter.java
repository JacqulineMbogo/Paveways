package com.paveways.Listings;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paveways.R;

import java.util.List;

public class Listing_Details_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Listing_Details_Model> mListingsList;
    private String TAG ="listings adapter";
    private int mScrenwith;

    public Listing_Details_Adapter(Context context, List<Listing_Details_Model> list, int screenwidth ){
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


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listing, parent,false);
        Log.e(TAG, "  view created ");
        return new Listing_Details_Adapter.ListingsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Listing_Details_Model model = mListingsList.get(position);
        Log.e(TAG, model.getImg_ulr());
        ((Listing_Details_Adapter.ListingsHolder) holder).name.setText(model.getL_name());




        Glide.with(mContext)
                .load("https://demkadairy.co.ke/paveways/admin/property/"+ model.getImg_ulr())
                .into(((ListingsHolder) holder).prod_image);
        // imageview glider lib to get imagge from url


        ((Listing_Details_Adapter.ListingsHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  prod_id "+String.valueOf(model.getL_id()));

                Intent intent = new Intent(mContext, Listing_Details.class);
                intent.putExtra("prod_id", model.getL_id());


                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mListingsList.size();
    }

    public void clear() {
        mListingsList.clear();
        notifyDataSetChanged();
    }

}