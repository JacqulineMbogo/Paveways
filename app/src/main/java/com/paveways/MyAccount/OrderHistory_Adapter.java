package com.paveways.MyAccount;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.paveways.R;
import com.paveways.Staff.Auth.StaffLogin;
import com.paveways.StaffProfile;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddAppointment;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderHistory_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<orderhistory_model> history_model;
    private Context mContext;
    private String TAG = "orderhistory_adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();
    SharedPreferenceActivity sharedPreferenceActivity;

    public OrderHistory_Adapter (Context context, List<orderhistory_model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);

    }
    private class OrderHistoryItemView extends RecyclerView.ViewHolder {
        TextView order_id,  order_shipping, order_price, order_date, order_viewdetails,generate_receipts,getreceipts;
        LinearLayout icons;
        ImageView approve,reject;


        public OrderHistoryItemView(View itemView) {
            super(itemView);
            order_id = (TextView) itemView.findViewById(R.id.order_id);
            order_price = (TextView) itemView.findViewById(R.id.order_price);
            order_shipping = (TextView) itemView.findViewById(R.id.order_shipping);
            order_date = (TextView) itemView.findViewById(R.id.order_date);
            order_viewdetails = (TextView) itemView.findViewById(R.id.order_viewdetails);
            generate_receipts = (TextView) itemView.findViewById(R.id.receipts);
            getreceipts = (TextView) itemView.findViewById(R.id.getreceipts);
            icons = itemView.findViewById(R.id.icons);
            approve = itemView.findViewById(R.id.approve);
            reject = itemView.findViewById(R.id.reject);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_orderhistory_item, parent,false);
        //Log.e(TAG, "  view created ");
        return new OrderHistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final orderhistory_model model =  history_model.get(position);

        ((OrderHistoryItemView) holder).order_id.setText(model.getorder_id());


        ((OrderHistoryItemView) holder).order_date.setText(model.getdate());
        ((OrderHistoryItemView) holder).generate_receipts.setText(model.getprice());


        if(Objects.equals(model.getsecurecode(), "00") && Objects.equals(model.getprice(), "Pending")){

            ((OrderHistoryItemView) holder).icons.setVisibility(View.VISIBLE);

        }
        // ((OrderAddressItemView) holder).address_layoutmain.setTag( model.getaddress_id());
        ((OrderHistoryItemView) holder).order_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  user select the order id " + model.getorder_id() );


                Intent intent = new Intent(mContext, OrderHistory_ViewDetails.class);
                intent.putExtra("order_id", model.getorder_id());
                intent.putExtra("address", "00");
                mContext.startActivity(intent);

            }
        });
        ((OrderHistoryItemView) holder).approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrderDetails("11",model.getorder_id());
            }
        });

        ((OrderHistoryItemView) holder).reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrderDetails("00",model.getorder_id());
            }
        });

                ((OrderHistoryItemView) holder).getreceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  user select the order id " + model.getorder_id() );


                Intent intent = new Intent(mContext, generate_receipts.class);
                intent.putExtra("order_id", model.getorder_id());

                mContext.startActivity(intent);
            }
        });

    /* ((OrderHistoryItemView) holder).generate_receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  user select the order id " + model.getorder_id() );


                Intent intent = new Intent(mContext, generate_receipts.class);
                intent.putExtra("order_id", model.getorder_id());

                mContext.startActivity(intent);

            }
        });

*/
    }

    public void editOrderDetails(String securecode,String order_id){

        final android.app.AlertDialog progressbar = AppUtilits.createProgressBar(mContext,"Please wait..");

        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddAppointment> call = service.editOrderDetailsCall(securecode,order_id, sharedPreferenceActivity.getItem(Constant.USER_DATA));
            call.enqueue(new Callback<AddAppointment>() {
                @Override
                public void onResponse(Call<AddAppointment> call, Response<AddAppointment> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());

                            Intent intent = new Intent(mContext, StaffProfile.class);
                            mContext.startActivity(intent);



                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            Intent intent = new Intent(mContext, OrderHistory.class);
                            mContext.startActivity(intent);
                            AppUtilits.displayMessage(mContext, "Unable to reject order please try again  ");
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(mContext, "Something went wrong. Please try again");
                    }


                }

                @Override
                public void onFailure(Call<AddAppointment> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(mContext, mContext.getString(R.string.fail_add_to_cart));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }
}

