package com.example.statefull;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

interface onSwitchListener {
    void onSwitchClick(int position);
}

interface onDeleteListener {
    void onDeleteClick(int position);
}

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    List<Reminder> reminders;
    onSwitchListener slistener;
    onDeleteListener dlistener;

    ReminderAdapter(List<Reminder> reminders, onSwitchListener switchListener, onDeleteListener deleteListener) {
        this.reminders = reminders;
        slistener = switchListener;
        dlistener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reminderView = inflater.inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(reminderView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderAdapter.ViewHolder holder, final int p) {

        final Reminder reminder = reminders.get(p);
        String time = "";
        int h = reminder.getHour();
        int m = reminder.getMinute();

        if (h < 10) {
            time = "0" + h + ":";
        } else {
            time = h + ":";
        }
        if (m < 10) {
            time = time + "0" + m;
        } else {
            time = time + m;
        }
        holder.viewid = reminder._id;
        holder.time.setText(time);
        holder.meridian.setText(reminder.meridian);

        Log.d("Switch status", Boolean.toString(reminder.active));
        holder.aSwitch.setChecked(reminder.active);

    }

    @Override
    public int getItemCount() {
        int x = DatabaseManager.databaseManager.getReminderTableSize();
        Log.d("ItemCount", Integer.toString(x));
        return x;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int viewid;
        TextView time;
        TextView meridian;
        Switch aSwitch;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            meridian = itemView.findViewById(R.id.ampm);
            aSwitch = itemView.findViewById(R.id.a_switch);
            delete = itemView.findViewById(R.id.delete_reminder);
            aSwitch.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.a_switch) {
                slistener.onSwitchClick(viewid);
            } else if (view.getId() == R.id.delete_reminder) {
                dlistener.onDeleteClick(viewid);
            }
        }
    }


}
