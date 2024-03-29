package com.paveways.Appointment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Listings.Listing_Details;
import com.paveways.Listings.Listing_Details_Adapter;
import com.paveways.R;
import com.paveways.Staff.StaffAppointment.StaffAppointment_Adapter;
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

public class Appointment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Appointment_Model> history_model;
    private Context mContext;
    private String TAG = "appointment model adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    SharedPreferenceActivity sharedPreferenceActivity;

    public Appointment_Adapter (Context context, List<Appointment_Model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);

    }
    private class HistoryItemView extends RecyclerView.ViewHolder {
        TextView  date_time, status, comment, property,name,email,phone ;
        ImageView edit, cancel;


        public HistoryItemView(View itemView) {
            super(itemView);
            date_time = (TextView) itemView.findViewById(R.id.date_time);
            status = (TextView) itemView.findViewById(R.id.status);
            comment = (TextView) itemView.findViewById(R.id.comment);
            property = (TextView) itemView.findViewById(R.id.property);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            cancel = (ImageView) itemView.findViewById(R.id.cancel);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            phone = (TextView) itemView.findViewById(R.id.phone);
                   }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_appointment_history_item, parent,false);
        //Log.e(TAG, "  view created ");
        return new HistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final Appointment_Model model =  history_model.get(position);

        ((HistoryItemView) holder).date_time.setText(model.getDate() + ' ' + model.getTime());


        ((HistoryItemView) holder).status.setText(model.getStatus());
        ((HistoryItemView) holder).comment.setText(model.getComment());
        ((HistoryItemView) holder).property.setText(model.getTitle());
        ((HistoryItemView) holder).name.setText("Name : " + model.getFullname());
        ((HistoryItemView) holder).email.setText("Email : " + model.getEmail());
        ((HistoryItemView) holder).phone.setText("Phone : " +model.getPhone());

        if(!Objects.equals(model.getStatus(), "Pending")){
            ((HistoryItemView) holder).edit.setVisibility(View.INVISIBLE);
            ((HistoryItemView) holder).cancel.setVisibility(View.INVISIBLE);
        }
        ((HistoryItemView) holder).edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, Appointment_Activity.class);
                intent.putExtra("listing_id","0");
                intent.putExtra("appointment_id", model.getAppointment_id());
                intent.putExtra("edit", true);


                mContext.startActivity(intent);

            }
        });

        ((HistoryItemView) holder).cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to cancel this appointment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bookAppointment("0", model.getAppointment_id());
                            }
                        }).setNegativeButton("No", null)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }

    public void bookAppointment(String securecode,String appointment_id){

        final android.app.AlertDialog progressbar = AppUtilits.createProgressBar(mContext," Please wait..");

        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddAppointment> call = service.addAppointmentCall(securecode, "",sharedPreferenceActivity.getItem(Constant.USER_DATA),"","",appointment_id ,"" );
            call.enqueue(new Callback<AddAppointment>() {
                @Override
                public void onResponse(Call<AddAppointment> call, Response<AddAppointment> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());


                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, "Unable to cancel appointment please try again  ");
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_error));
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

}
