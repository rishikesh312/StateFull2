package com.example.statefull;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class StateFragment extends Fragment implements MoodDialogFragment.MoodDialogListener {

    String choosen;
    Fragment fragment;
    ArrayList<Entry> yValues = new ArrayList<>();
    private LineChart lineChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_states, container, false);
        fragment = this;

        FloatingActionButton fab = view.findViewById(R.id.fab_add_emotion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                if (context != null) {
                    MoodDialogFragment mood = new MoodDialogFragment();
                    mood.fragment = fragment;
                    mood.show(getFragmentManager(), "Mood");
                }

            }
        });
        initGraph(view);

        return view;
    }

    private void initGraph(View view) {
        lineChart = view.findViewById(R.id.line_chart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMaximum(20);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getXAxis().setTextColor(-1);
        lineChart.getAxisLeft().setTextColor(-1);

        LimitLine lowerlimit = new LimitLine(6f, "Negative");

        updateGraphs();

    }

    @Override
    public void onItemClicked(String x) {
        String[] s = getResources().getStringArray(R.array.moods);
        int i;
        for (i = 0; i < s.length; i++) {
            if (x.equals(s[i])) {
                break;
            }
        }
        int val = 17 - i;
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long time = date.getTime();
        DatabaseManager.databaseManager.moodEntry(time, val);
        Toast.makeText(getContext(), x, Toast.LENGTH_LONG).show();
        updateGraphs();
    }

    private void updateGraphs() {
        TreeMap<Long, Integer> entries = DatabaseManager.databaseManager.getMoodEntries();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long initial = 0;
        long diff = 0;
        yValues = new ArrayList<>();
        for (long x : entries.keySet()) {
            Log.d("X=", Long.toString(x));
            if (initial == 0) {
                initial = x;
            } else {
                diff = (x - initial) / 1000;
                diff /= 5;
            }
            int val = entries.get(x);
            yValues.add(new Entry(diff, val));
        }
        LineDataSet dtset = new LineDataSet(yValues, "Moods");
        dtset.setFillAlpha(0);
        dtset.setColor(-16711767);
        dtset.setLineWidth(2);
        dtset.setValueTextColor(Color.WHITE);
        ArrayList<ILineDataSet> datasets = new ArrayList<>();
        datasets.add(dtset);
        LineData data = new LineData(datasets);
        lineChart.setData(data);
    }
}
