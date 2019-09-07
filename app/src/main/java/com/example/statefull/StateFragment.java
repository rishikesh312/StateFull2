package com.example.statefull;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class StateFragment extends Fragment implements MoodDialogFragment.MoodDialogListener {

    String choosen;
    Fragment fragment;
    ArrayList<BarEntry> yValues = new ArrayList<>();
    private BarChart barChart;

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
        barChart = view.findViewById(R.id.mood_chart);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMaximum(20);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setTextColor(Color.BLACK);
        barChart.getAxisLeft().setTextColor(-1);
        barChart.getAxisLeft().setTextColor(Color.BLACK);


        LimitLine lowerlimit = new LimitLine(5f);
        lowerlimit.setLineWidth(4);
        lowerlimit.enableDashedLine(10f, 10f, 1f);
        lowerlimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerlimit.setTextSize(15f);
        lowerlimit.setLineColor(Color.RED);

        LimitLine upperlimit = new LimitLine(15f);
        upperlimit.setLineWidth(4);
        upperlimit.enableDashedLine(10f, 10f, 1f);
        upperlimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        upperlimit.setTextSize(15f);
        upperlimit.setLineColor(Color.GREEN);

        barChart.getAxisLeft().addLimitLine(lowerlimit);
        barChart.getAxisLeft().addLimitLine(upperlimit);
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
        int i = 0;
        for (long x : entries.keySet()) {
            /*Log.d("X=", Long.toString(x));
            if (initial == 0) {
                initial = x;
            } else {
                diff = (x - initial) / 1000;
                diff /= 5;
            }*/
            int val = entries.get(x);
            BarEntry barEntry = new BarEntry(i, val);
            yValues.add(barEntry);
            i++;
        }
        yValues.add(new BarEntry(i, 0));


        BarDataSet dtset = new BarDataSet(yValues, "Moods");
        dtset.setBarBorderWidth(1f);
        dtset.setColor(Color.rgb(40, 0, 30));
        dtset.setValueTextColor(Color.BLACK);
        BarData data = new BarData(dtset);
        barChart.setData(data);
        barChart.getXAxis().setGranularity(3f);
        barChart.setMaxVisibleValueCount(6);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.canScrollHorizontally(BarChart.SCROLL_AXIS_HORIZONTAL);
        barChart.animateXY(1000, 1000);
    }
}
