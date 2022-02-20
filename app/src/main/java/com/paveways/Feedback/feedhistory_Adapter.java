package com.paveways.Feedback;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paveways.R;

import java.util.ArrayList;
import java.util.List;


public class feedhistory_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<feedhistory_model> history_model;
    private Context mContext;
    private String TAG = "feedhistory_adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();

    public feedhistory_Adapter (Context context, List<feedhistory_model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;

    }
    private class FeedHistoryItemView extends RecyclerView.ViewHolder {
        TextView  comment, reply, commentdate, replydate;


        public FeedHistoryItemView(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.feedback);
            reply = (TextView) itemView.findViewById(R.id.reply);
            commentdate = (TextView) itemView.findViewById(R.id.feeddate);
            replydate = (TextView) itemView.findViewById(R.id.replydate);



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


        ((FeedHistoryItemView)  holder).reply.setText(model.getReply());
        ((FeedHistoryItemView)  holder).commentdate.setText(model.getCommentdate());


        ((FeedHistoryItemView)  holder).replydate.setText(model.getReplydate());



    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }
}

