package com.paveways.Feedback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.paveways.Appointment.Appointment_Activity;
import com.paveways.Appointment.Appointment_Adapter;
import com.paveways.R;
import com.paveways.Utility.SharedPreferenceActivity;

import java.util.List;

public class Staff_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Staff_Model> staff_model;
    SharedPreferenceActivity sharedPreferenceActivity;
    private Context mContext;
    private String TAG = "staff_adapter";

    public Staff_Adapter(Context mContext, List<Staff_Model> staff_model) {
        this.staff_model = staff_model;
        sharedPreferenceActivity = new SharedPreferenceActivity(mContext);
        this.mContext = mContext;
    }

    private class StaffItemView extends RecyclerView.ViewHolder {
        TextView name, department;
        CardView card;


        public StaffItemView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            department = (TextView) itemView.findViewById(R.id.department);
            card = itemView.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_chat_item, parent, false);
        //Log.e(TAG, "  view created ");
        return new StaffItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view " + position);
        final Staff_Model model = staff_model.get(position);

        ((StaffItemView) holder).name.setText(model.getName());
        ((StaffItemView) holder).department.setText(model.getDepartment());
        ((StaffItemView) holder).card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, FeedbackHistory.class);
                intent.putExtra("user",model.getId());
                intent.putExtra("name",model.getName());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return staff_model.size();
    }
}
