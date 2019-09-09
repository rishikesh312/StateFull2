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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class StateFragment extends Fragment implements MoodDialogFragment.MoodDialogListener {

    String choosen;
    Fragment fragment;
    ArrayList<BarEntry> moodYValues = new ArrayList<>();
    ArrayList<BarEntry> dayYValues = new ArrayList<>();

    private BarChart barChart;
    private BarChart dayBarChart;

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

        dayBarChart = view.findViewById(R.id.day_chart);
        dayBarChart.setDragEnabled(true);
        dayBarChart.setScaleEnabled(true);
        dayBarChart.getAxisRight().setEnabled(false);
        dayBarChart.getAxisLeft().setAxisMaximum(20);
        dayBarChart.getAxisLeft().setAxisMinimum(0);
        dayBarChart.getXAxis().setTextColor(Color.BLACK);
        dayBarChart.getAxisLeft().setTextColor(-1);
        dayBarChart.getAxisLeft().setTextColor(Color.BLACK);

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
        dayBarChart.getAxisLeft().addLimitLine(lowerlimit);
        dayBarChart.getAxisLeft().addLimitLine(upperlimit);
        updateMoodGraphs();
        updateDayGraphs();

    }

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
        updateMoodGraphs();
        updateDayGraphs();
    }

    private void updateMoodGraphs() {
        TreeMap<Long, Integer> entries = DatabaseManager.databaseManager.getMoodEntries();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long initial = 0;
        long diff = 0;
        moodYValues = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        int i = 0;
        for (long x : entries.keySet()) {
            int val = entries.get(x);
            BarEntry barEntry = new BarEntry(i, val);
            moodYValues.add(barEntry);

            i++;
        }
        moodYValues.add(new BarEntry(i, 0));
        //colors.add(Color.WHITE);

        BarDataSet dtset = new BarDataSet(moodYValues, "Moods");
        dtset.setColor(Color.CYAN);
        dtset.setBarBorderWidth(1f);
        dtset.setValueTextColor(Color.BLACK);
        dtset.setValueTextSize(5);
        BarData data = new BarData(dtset);
        barChart.setData(data);
        barChart.getXAxis().setGranularity(3f);
        barChart.setMaxVisibleValueCount(6);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.canScrollHorizontally(BarChart.SCROLL_AXIS_HORIZONTAL);
        barChart.animateXY(800, 500);

    }

    private void updateDayGraphs() {
        TreeMap<String, List<Integer>> entries = DatabaseManager.databaseManager.getDailyAverage();
        dayYValues = new ArrayList<>();

        int i = 0;
        for (String x : entries.keySet()) {
            int val = entries.get(x).get(0);
            BarEntry barEntry = new BarEntry(i, val);
            dayYValues.add(barEntry);
            i++;
        }
        dayYValues.add(new BarEntry(i, 0));
        BarDataSet dtset = new BarDataSet(dayYValues, "Moods");
        dtset.setColor(Color.MAGENTA);
        dtset.setBarBorderWidth(1f);
        dtset.setValueTextColor(Color.BLACK);
        dtset.setValueTextSize(5);
        BarData data = new BarData(dtset);
        dayBarChart.setData(data);
        dayBarChart.getXAxis().setGranularity(3f);
        dayBarChart.setMaxVisibleValueCount(6);
        dayBarChart.getAxisLeft().setTextColor(Color.WHITE);
        dayBarChart.getXAxis().setTextColor(Color.WHITE);
        dayBarChart.canScrollHorizontally(BarChart.SCROLL_AXIS_HORIZONTAL);
        dayBarChart.animateXY(800, 500);

    }

    class MyBarDataSet extends BarDataSet {
        MyBarDataSet(List<BarEntry> yVals, String label) {
            super(yVals, label);
        }

        @Override
        public int getColor(int index) {
            Log.d("Color:", index + " " + getEntryForIndex(index).getY());
            if (getEntryForIndex(index).getY() < 6) {
                return Color.RED;
            } else if (getEntryForIndex(index).getY() < 12) {
                return Color.CYAN;
            } else if (getEntryForIndex(index).getY() < 14) {
                return Color.YELLOW;
            } else if (getEntryForIndex(index).getY() < 20) {
                return Color.GREEN;
            } else {
                return Color.MAGENTA;
            }
        }

    }
}
