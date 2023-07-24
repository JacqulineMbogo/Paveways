package com.paveways.Tenant.Services;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.paveways.R;
import com.paveways.Staff.StaffAppointment.StaffAppointment_Adapter;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.AddAppointment;
import com.paveways.WebResponse.RequestService;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Services_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Services_Model> services_model;
    private Context mContext;
    private String TAG = "services model adapter";
    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    SharedPreferenceActivity sharedPreferenceActivity;

    public Services_Adapter (Context context, List<Services_Model> serviceModels) {
        this.services_model = serviceModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);
    }
    private class HistoryItemView extends RecyclerView.ViewHolder {
        TextView name, comment, requestService ;

        public HistoryItemView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            requestService = itemView.findViewById(R.id.requestService);

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_services_history_item, parent,false);
        //Log.e(TAG, "  view created ");
        return new Services_Adapter.HistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "bind view "+ position);
        final Services_Model model =  services_model.get(position);


        ((HistoryItemView) holder).name.setText(model.getName());
        ((HistoryItemView) holder).comment.setText(model.getDescription());

        ((HistoryItemView) holder).requestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // Inflate the layout XML file containing the EditText
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                EditText editText = dialogView.findViewById(R.id.editText);
                builder.setTitle("Description")
                        .setMessage("To provide you with the best assistance, kindly provide a detailed description of the issue you are facing. ")
                        .setView(dialogView)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userInput = editText.getText().toString();
                                requestforService(sharedPreferenceActivity.getItem(Constant.USER_DATA),userInput,model.getId(), mContext);
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }
    public static void requestforService(String user, String comment,String serviceID, Context mContext) {
        final android.app.AlertDialog progressbar = AppUtilits.createProgressBar(mContext," Please wait..");

        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<RequestService> call = service.requestServiceCall("123", user,serviceID,comment);
            call.enqueue(new Callback<RequestService>() {
                @Override
                public void onResponse(Call<RequestService> call, Response<RequestService> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());


                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, "Unable to request this service.Please try again  ");
                        }
                    }else {
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
    @Override
    public int getItemCount() {
        return services_model.size();
    }
}
