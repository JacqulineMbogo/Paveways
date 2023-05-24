package com.paveways.Users;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Appointment.Appointment_Activity;
import com.paveways.R;
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

public class Users_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Users_Model> history_model;
    private Context mContext;
    private String TAG = "users model adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    SharedPreferenceActivity sharedPreferenceActivity;

    public Users_Adapter(Context context, List<Users_Model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);

    }
    private class HistoryItemView extends RecyclerView.ViewHolder {
        TextView  name, phone,email,date,status , comment ;
        ImageView approve, reject;


        public HistoryItemView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            comment = (TextView) itemView.findViewById(R.id.comment);
            approve = (ImageView) itemView.findViewById(R.id.approve);
            reject = (ImageView) itemView.findViewById(R.id.reject);
                   }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_history_item, parent,false);
        //Log.e(TAG, "  view created ");
        return new HistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final Users_Model model =  history_model.get(position);

        ((HistoryItemView) holder).name.setText(model.getFullname());
        ((HistoryItemView) holder).phone.setText(model.getPhone());
        ((HistoryItemView) holder).email.setText(model.getEmail());
        ((HistoryItemView) holder).date.setText(model.getDate());
        ((HistoryItemView) holder).status.setText(model.getStatus());
        ((HistoryItemView) holder).comment.setText(model.getComment());

        if(!Objects.equals(model.getStatus(), "Pending")) {
            ((HistoryItemView) holder).approve.setVisibility(View.INVISIBLE);
            ((HistoryItemView) holder).reject.setVisibility(View.INVISIBLE);
        }

            ((HistoryItemView) holder).approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to Approve this user?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editUserDetails("1", model.getUser_id());
                                }
                            }).setNegativeButton("No", null)
                            .show();

                }
            });

            ((HistoryItemView) holder).reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to reject this user?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editUserDetails("0", model.getUser_id());
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

    public void editUserDetails(String securecode,String user_id){

        final android.app.AlertDialog progressbar = AppUtilits.createProgressBar(mContext,"Please wait..");

        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddAppointment> call = service.editUserDetailsCall(securecode, sharedPreferenceActivity.getItem(Constant.USER_DATA),user_id  );
            call.enqueue(new Callback<AddAppointment>() {
                @Override
                public void onResponse(Call<AddAppointment> call, Response<AddAppointment> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());


                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, "Unable to reject user please try again  ");
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
