package com.paveways.Tenant.Services;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Appointment.Appointment_Adapter;
import com.paveways.R;
import com.paveways.Staff.StaffAppointment.StaffAppointment_Adapter;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.RequestService;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service_Requests_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Service_Requests_Model> history_model;
    private Context mContext;
    private String TAG = "Service Requests model adapter";

    private ArrayList<RelativeLayout> addrlayoutsList = new ArrayList<>();
    SharedPreferenceActivity sharedPreferenceActivity;

    public Service_Requests_Adapter(Context context, List<Service_Requests_Model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);

    }

    private class HistoryItemView extends RecyclerView.ViewHolder {
        TextView service, status, comment, user_comment, name, email, phone, amount, create_date, update_date;
        ImageView edit, cancel;


        public HistoryItemView(View itemView) {
            super(itemView);
            service = (TextView) itemView.findViewById(R.id.service);
            status = (TextView) itemView.findViewById(R.id.status);
            comment = (TextView) itemView.findViewById(R.id.comment);
            amount = itemView.findViewById(R.id.amount);
            user_comment = (TextView) itemView.findViewById(R.id.user_comment);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            cancel = (ImageView) itemView.findViewById(R.id.cancel);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            phone = (TextView) itemView.findViewById(R.id.phone);
            create_date = (TextView) itemView.findViewById(R.id.create_date);
            update_date = (TextView) itemView.findViewById(R.id.update_date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_service_request_history_item, parent, false);
        //Log.e(TAG, "  view created ");
        return new Service_Requests_Adapter.HistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view " + position);
        final Service_Requests_Model model = history_model.get(position);

        ((HistoryItemView) holder).service.setText("Service : " + model.getService());
        ((HistoryItemView) holder).status.setText(model.getStatus());
        ((HistoryItemView) holder).user_comment.setText(model.getUser_comment());
        ((HistoryItemView) holder).comment.setText(model.getStaff_comment());
        ((HistoryItemView) holder).amount.setText("Total Cost - Ksh : " + model.getAmount());
        ((HistoryItemView) holder).name.setText("Name : " + model.getFullname());
        ((HistoryItemView) holder).email.setText("Email : " + model.getEmail());
        ((HistoryItemView) holder).phone.setText("Phone : " + model.getPhone());
        ((HistoryItemView) holder).create_date.setText("Requested On : " + model.getCreate_date());
        ((HistoryItemView) holder).update_date.setText("Updated On : " + model.getUpdate_date());

        if (Objects.equals(model.getStatus(), "Pending")) {
            ((HistoryItemView) holder).cancel.setVisibility(View.VISIBLE);
        }
        ((HistoryItemView) holder).cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to cancel this service?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editService("0", sharedPreferenceActivity.getItem(Constant.USER_DATA), "", model.getRequestid(),"", mContext);
                            }
                        }).setNegativeButton("No", null)
                        .show();
            }
        });

        if(!sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()) {
            if (Objects.equals(model.getStatus(), "Pending")) {
                ((HistoryItemView) holder).edit.setVisibility(View.VISIBLE);
            }
        }
        ((HistoryItemView) holder).edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // Inflate the layout XML file containing the EditText
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.service_layout, null);
                EditText editText = dialogView.findViewById(R.id.editText);
                EditText totalCost = dialogView.findViewById(R.id.totalCost);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to edit this service? If so, add comments to give more details on the service provided: ")
                        .setView(dialogView)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userInput = editText.getText().toString();
                                String amount = totalCost.getText().toString();
                                editService("900", sharedPreferenceActivity.getItem(Constant.USER_DATA), userInput, model.getRequestid(), amount, mContext);
                            }
                        }).setNegativeButton("No", null)
                        .show();

            }
        });
        if (!model.getAmount().isEmpty() || !Objects.equals(model.getAmount(), "")) {
            ((HistoryItemView) holder).amount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }

    public static void editService(String securecode, String user, String comment, String serviceID,String totalCost, Context mContext) {
        final android.app.AlertDialog progressbar = AppUtilits.createProgressBar(mContext, " Please wait..");

        if (!NetworkUtility.isNetworkConnected(mContext)) {
            AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<RequestService> call = service.requestServiceCall(securecode, user, serviceID, comment,totalCost);
            call.enqueue(new Callback<RequestService>() {
                @Override
                public void onResponse(Call<RequestService> call, Response<RequestService> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());


                        } else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, "Unable to request this service.Please try again  ");
                        }
                    } else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<RequestService> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(mContext, mContext.getString(R.string.fail_add_to_cart));
                }
            });
        }
    }
}
