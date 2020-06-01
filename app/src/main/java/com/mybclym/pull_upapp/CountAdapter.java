package com.mybclym.pull_upapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mybclym.pull_upapp.database.TimerEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.CountViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private List<TimerEntry> mTimerEntries;
    private Context mContext;

    ItemClickListener mItemClickListener;

    public CountAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public CountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new CountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountViewHolder holder, int position) {
        TimerEntry timer = mTimerEntries.get(position);
        holder.et_time.setText(String.valueOf(timer.getTime()));
        holder.et_numberOfRepeats.setText(String.valueOf(timer.getRepeats()));
    }

    @Override
    public int getItemCount() {
        if (mTimerEntries == null) return 0;
        return mTimerEntries.size();
    }

    public void setTimerEntries(List<TimerEntry> timers) {
        mTimerEntries = timers;
        notifyDataSetChanged();
    }

    public List<TimerEntry> getTimers() {
        return mTimerEntries;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class CountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText et_time, et_numberOfRepeats;
        ImageView done_lable;

        public CountViewHolder(@NonNull View itemView) {
            super(itemView);
            et_time = itemView.findViewById(R.id.et_time);
            et_numberOfRepeats = itemView.findViewById(R.id.et_number_of_exercises);
            done_lable = itemView.findViewById(R.id.image_time_finish);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mTimerEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
