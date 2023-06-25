package com.paveways.Feedback;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.paveways.R;
import com.paveways.Staff.StaffAppointment.StaffAppointment_Adapter;
import com.paveways.Utility.AppUtilits;
import com.paveways.Utility.Constant;
import com.paveways.Utility.NetworkUtility;
import com.paveways.Utility.SharedPreferenceActivity;
import com.paveways.WebResponse.feedbackAPI;
import com.paveways.WebServices.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class feedhistory_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<feedhistory_model> history_model;
    SharedPreferenceActivity sharedPreferenceActivity;
    private Context mContext;
    private String TAG = "feedhistory_adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();

    public feedhistory_Adapter (Context context, List<feedhistory_model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

    }
    private class FeedHistoryItemView extends RecyclerView.ViewHolder {
        TextView  comment, reply, commentdate, replydate,replytext;


        public FeedHistoryItemView(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.feedback);
            reply = (TextView) itemView.findViewById(R.id.reply);
            commentdate = (TextView) itemView.findViewById(R.id.feeddate);
            replydate = (TextView) itemView.findViewById(R.id.replydate);
            replytext = (TextView) itemView.findViewById(R.id.replytext);



        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_chat, parent,false);
        //Log.e(TAG, "  view created ");
        return new FeedHistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final feedhistory_model model =  history_model.get(position);

        ((FeedHistoryItemView) holder).comment.setText(model.getComment());

        if(Objects.equals(model.getReply(), "...")) {
            ((FeedHistoryItemView) holder).reply.setVisibility(View.GONE);
            ((FeedHistoryItemView)  holder).replydate.setVisibility(View.GONE);
            if(!sharedPreferenceActivity.getItem(Constant.DEPARTMENT).isEmpty()){
                ((FeedHistoryItemView)  holder).replytext.setVisibility(View.VISIBLE);
            }
        }else {
            ((FeedHistoryItemView) holder).reply.setText(model.getReply());
            ((FeedHistoryItemView)  holder).replydate.setText(model.getReplydate());
        }



        ((FeedHistoryItemView)  holder).commentdate.setText(model.getCommentdate());

        ((FeedHistoryItemView)  holder).replytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);

                // Inflate the layout XML file containing the EditText
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                EditText editText = dialogView.findViewById(R.id.editText);
                builder.setTitle("Reply")
                        .setView(dialogView)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userInput = editText.getText().toString();
                                submitFeedbackReply("00", userInput,model.getId());
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }

    public void submitFeedbackReply(String securecode,String reply, String message_id){
        final AlertDialog progressbar = AppUtilits.createProgressBar(mContext,"Please Wait");
        if (!NetworkUtility.isNetworkConnected(mContext)){
            Toast.makeText(mContext,"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<feedbackAPI> feedbackAPICall=serviceWrapper.feedbackcall(securecode, message_id,
                    reply,String.valueOf(sharedPreferenceActivity.getItem(Constant.USER_DATA)),"");
            feedbackAPICall.enqueue(new Callback<feedbackAPI>() {
                @Override
                public void onResponse(Call<feedbackAPI> call, Response<feedbackAPI> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg() );

                            Intent intent = new Intent(mContext, FeedbackHistory.class);
                            mContext.startActivity(intent);

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(mContext, response.body().getMsg());

                        }

                    } else {
                        Toast.makeText(mContext,"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<feedbackAPI> call, Throwable throwable) {

                    Toast.makeText(mContext,"Failed to send reply",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);
                }
            });

        }

    }
}

