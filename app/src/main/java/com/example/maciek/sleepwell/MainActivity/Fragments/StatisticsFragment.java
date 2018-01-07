package com.example.maciek.sleepwell.MainActivity.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maciek.sleepwell.R;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.jjoe64.graphview.DefaultLabelFormatter;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GridLabelRenderer;
//import com.jjoe64.graphview.helper.StaticLabelsFormatter;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Maciek on 27.04.2017.
 */

public class StatisticsFragment extends Fragment {

    private LineChart mChart;
    private List<Float> xData;
    private List<String> yDataNotFormatted;
    private List<Integer> yDataFormatted;

    private static final String AWAKE = "AWAKE";
    private static final String NREM1 = "NREM1";
    private static final String NREM2 = "NREM2";
    private static final String NREM3 = "NREM3";
    private static final String REM = "REM";

    final String[] quarters = new String[] { "", NREM3, NREM2, NREM1, REM, AWAKE, "" };
    private IAxisValueFormatter formatter;

    private List<Entry> sleepLineChart;
    private LineDataSet lineDataSet;

    private int amountOfPoints;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment,container,false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.statistics_fragment);
        mChart = (LineChart) view.findViewById(R.id.chart);


        sleepLineChart = new ArrayList<>();

        xData = new ArrayList<>();
        yDataNotFormatted = new ArrayList<>();
        yDataFormatted = new ArrayList<>();
        amountOfPoints = 30;

        Random random = new Random();
        double rand = 0;

        Date date = new Date();
        for(int i = 0;i<amountOfPoints;i++){
            date.setTime(SystemClock.currentThreadTimeMillis());
//            xData.add(date.getHours());
            xData.add((float)i);

            rand = random.nextDouble();
            if(rand>=0 && rand<0.20){
                yDataNotFormatted.add(AWAKE);
            }
            else if(rand>=0.20 && rand<0.40){
                yDataNotFormatted.add(NREM1);
            }
            else if(rand>=0.40 && rand<0.60){
                yDataNotFormatted.add(NREM2);
            }
            else if(rand>=0.60 && rand<0.80){
                yDataNotFormatted.add(NREM3);
            }
            else{
                yDataNotFormatted.add(REM);
            }
        }
        for(int i = 0;i<amountOfPoints;i++){
            switch(yDataNotFormatted.get(i)){
                case AWAKE:
                    sleepLineChart.add(new Entry(xData.get(i), 5f));
                    yDataFormatted.add(5);
                    break;
                case REM:
                    sleepLineChart.add(new Entry(xData.get(i), 4f));
                    yDataFormatted.add(4);
                    break;
                case NREM1:
                    sleepLineChart.add(new Entry(xData.get(i), 3f));
                    yDataFormatted.add(3);
                    break;
                case NREM2:
                    sleepLineChart.add(new Entry(xData.get(i), 2f));
                    yDataFormatted.add(2);
                    break;
                case NREM3:
                    sleepLineChart.add(new Entry(xData.get(i), 1f));
                    yDataFormatted.add(1);
                    break;
//                final String[] quarters = new String[] { "", NREM3, NREM2, NREM1, REM, AWAKE, "" };
            }
        }
        for(int i = 0; i<amountOfPoints;i++){
            System.out.println("X: " + xData.get(i) + "     Y: " + yDataFormatted.get(i));
        }

        createChart();

        lineDataSet = new LineDataSet(sleepLineChart,"");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(true);
//        lineDataSet.setColor(Color.argb(175,255,255,255));
        lineDataSet.setColor(Color.argb(255,255,255,255));
        lineDataSet.setLineWidth(1f);

        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.animateXY(1200,1200);

    }


    private void createChart(){
        formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("");

        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.argb(0,0,0,0));

        LineData data = new LineData();
        data.setValueTextColor(Color.argb(200,255,255,255));

        mChart.setData(data);

        Legend legend = mChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);
        legend.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);

        leftAxis.setEnabled(true);
//        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(formatter);
        leftAxis.setAxisMinimum(0.5f);
        leftAxis.setAxisMaximum(5.5f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

}


