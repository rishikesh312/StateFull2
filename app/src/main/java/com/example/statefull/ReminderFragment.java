package com.example.statefull;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;


public class ReminderFragment extends Fragment implements onSwitchListener, onDeleteListener {

    private List<Reminder> reminders;
    private ReminderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);


        RecyclerView rvReminders = view.findViewById(R.id.rvreminders);
        reminders = DatabaseManager.databaseManager.getReminders();
        adapter = new ReminderAdapter(reminders, this, this);
        rvReminders.setAdapter(adapter);
        rvReminders.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    String meridian = "Am";
                                    if (hourOfDay > 12) {
                                        hourOfDay = hourOfDay - 12;
                                        meridian = "Pm";
                                    }
                                    DatabaseManager.databaseManager.fabAddReminder(hourOfDay, minute, meridian);
                                    adapter.reminders = DatabaseManager.databaseManager.getReminders();
                                    adapter.notifyDataSetChanged();
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });
        return view;
    }

    @Override
    public void onSwitchClick(int p) {
        Log.d("Logd Clicked Switch", Integer.toString(p));
        DatabaseManager.databaseManager.negatereminder(p);
        adapter.reminders = DatabaseManager.databaseManager.getReminders();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(int p) {
        Log.d("Logd Clicked Delete", Integer.toString(p));
        DatabaseManager.databaseManager.deletereminder(p);
        adapter.reminders = DatabaseManager.databaseManager.getReminders();
        adapter.notifyDataSetChanged();
    }

}
