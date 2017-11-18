package com.example.maciek.sleepwell.MainActivity.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maciek.sleepwell.R;
//import com.jjoe64.graphview.DefaultLabelFormatter;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GridLabelRenderer;
//import com.jjoe64.graphview.helper.StaticLabelsFormatter;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Created by Maciek on 27.04.2017.
 */

public class StatisticsFragment extends Fragment {
/*
    LineGraphSeries<DataPoint> inputData;
    GraphView graphStatisticsView;
    public int numberOfInputData;
    double xView,yView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment,container,false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        double yAxis=0;
        double xAxis=0;
        numberOfInputData = 500;

        graphStatisticsView = (GraphView) view.findViewById(R.id.graphViewStatistics);
        inputData = new LineGraphSeries<>();
        DataPoint[] values = new DataPoint[numberOfInputData];


        Random random = new Random();
        for(int i=0;i < numberOfInputData; i++){
            xAxis = xAxis + 1;
            yAxis = yAxis - random.nextInt(20);
            DataPoint vS = new DataPoint(xAxis,yAxis);
            values[i] = vS;
            inputData.appendData(values[i],true,numberOfInputData);
        }
        xView=xAxis;
        yView=yAxis;

        graphStatisticsView.addSeries(inputData);
        makeGraphStatisticsView();
    }


    protected void makeGraphStatisticsView(){

        // Ustawia oś y oraz oś x niezależnie od faktycznych danych
        //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphStatisticsView);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {"1", "2", "3", "4","5","6","7","8","9"});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"REM", "Deep", "Mid", "Shallow"});
        //graphStatisticsView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphStatisticsView.getViewport().setXAxisBoundsManual(true);
        graphStatisticsView.getViewport().setMinX(0);
        graphStatisticsView.getViewport().setMaxX(xView);

        graphStatisticsView.getViewport().setYAxisBoundsManual(true);
        graphStatisticsView.getViewport().setMinY(yView);
        graphStatisticsView.getViewport().setMaxY(0);

        inputData.setColor(Color.parseColor("#ffffff"));
        graphStatisticsView.getGridLabelRenderer().setTextSize(35f);
        graphStatisticsView.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#ffffff"));
        graphStatisticsView.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#ffffff"));
        graphStatisticsView.getGridLabelRenderer().setGridColor(Color.parseColor("#ffffff"));
        graphStatisticsView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);

        graphStatisticsView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                     if(value<=0 && value>-1500)
                     {
                         return "Shallow";
                     }
                    else if(value<=-1500 && value>-2500)
                     {
                         return "Middle";
                     }
                     else if(value<=-2500 && value>-3500)
                     {
                         return "Deep";
                     }
                    else
                     {
                         return "REM";
                     }

                }
            }
        });



        graphStatisticsView.getGridLabelRenderer().reloadStyles();
    }*/
}
