package com.paveways.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paveways.Listings.Listing_Details_Adapter;
import com.paveways.R;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddtoCart;
import com.paveways.WebResponse.EditCartItem;
import com.paveways.WebServices.ServiceWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Cartitem_Model> cartitem_models;
    private Context mContext;
    private String TAG ="cartAdapter";
    SharedPreferenceActivity sharedPreferenceActivity;
    public Cart_Adapter (Context context, List<Cartitem_Model> cartitemModels){
        this.cartitem_models = cartitemModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);

    }

    private class cartItemView extends RecyclerView.ViewHolder {
        ImageView prod_img, prod_delete;
        TextView prod_name, stock, prod_price,confirm;
        CardView cardView;
        EditText prod_qty;

        public cartItemView(View itemView) {
            super(itemView);
            prod_img = (ImageView) itemView.findViewById(R.id.prod_img);
            prod_name = (TextView) itemView.findViewById(R.id.prod_name);
            prod_price = (TextView) itemView.findViewById(R.id.prod_price);
            stock = (TextView) itemView.findViewById(R.id.stock);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            prod_delete = (ImageView) itemView.findViewById(R.id.cart_delete);
            prod_qty = (EditText) itemView.findViewById(R.id.prod_qty);
            confirm= itemView.findViewById(R.id.confirm);



        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartdetails_item, parent,false);
        Log.e(TAG, "  view created ");
        return new cartItemView(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Cartitem_Model model =  cartitem_models.get(position);

        ((cartItemView) holder).prod_name.setText(model.getProd_name());
        ((cartItemView) holder).prod_price.setText(model.getPrice());
        ((cartItemView) holder).prod_qty.setText(model.getQty());
        ((cartItemView) holder).stock.setText(model.getOld_price());


        Glide.with(mContext)
                .load("https://demkadairy.co.ke/paveways/admin/property/"+ model.getImg_ulr())
                .into(((cartItemView) holder).prod_img);

        // delete product from cart
        ((cartItemView) holder).prod_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProduct(model.getProd_id());
            }
        });

        ((cartItemView) holder).confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!((cartItemView) holder).prod_qty.getText().toString().trim().equals("") || !((cartItemView) holder).prod_qty.getText().toString().trim().equals("0")){

                        updateCartQty( model.getProd_id(), ((cartItemView) holder).prod_qty.getText().toString().trim());

                    }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartitem_models.size();
    }





    public void deleteProduct(String prod_id){

        final AlertDialog progressbar = AppUtilits.createProgressBar(mContext,"Removing item");
        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.deletecartprod("12345", sharedPreferenceActivity.getItem(Constant.USER_DATA), prod_id );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {

                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());

                            ((CartDetails) mContext).getUserCartDetails();
                            // update cart count
                            //    SharePreferenceUtils.getInstance().saveInt( Constant.CART_ITEM_COUNT,   SharePreferenceUtils.getInstance().getInteger(Constant.CART_ITEM_COUNT) -1);
                            //    AppUtilits.UpdateCartCount(mContext, CartDetails.mainmenu);

                        }else {
                            AppUtilits.displayMessage(mContext,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "  fail delete cart "+ t.toString());
                    AppUtilits.displayMessage(mContext, mContext.getString(R.string.fail_todeletecart));

                }
            });


        }

    }

    public void updateCartQty(String prod_id, String prod_qty){

        final AlertDialog progressbar =AppUtilits.createProgressBar(mContext,"Updating quantity");
        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.destroyDialog(progressbar);
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<EditCartItem>  call = service.editcartcartprodqty( "12345", sharedPreferenceActivity.getItem(Constant.USER_DATA), prod_id , prod_qty);
            call.enqueue(new Callback<EditCartItem>() {
                @Override
                public void onResponse(Call<EditCartItem> call, Response<EditCartItem> response) {

                    Log.e(TAG, " edit response "+ response.body().getMsg().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(mContext, response.body().getInformation().getStatus());

                            if (response.body().getInformation().getStatus().equalsIgnoreCase("successful update cart")){
                                ((CartDetails) mContext).cart_totalamt.setText(  "Ksh " + response.body().getInformation().getTotalprice());
                            }


                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(mContext, "Something went wrong");
                    }
                }


                @Override
                public void onFailure(Call<EditCartItem> call, Throwable t) {
                    Log.e(TAG, " edit fail "+ t.toString());
                    AppUtilits.displayMessage(mContext,  mContext.getString(R.string.fail_toeditcart));
                }
            });





        }

    }



}
