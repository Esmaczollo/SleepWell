package com.example.maciek.sleepwell.MainActivity.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maciek.sleepwell.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * Created by Maciek on 27.04.2017.
 */

public class StatisticsFragment extends Fragment {

    LineGraphSeries<DataPoint> inputData;
    GraphView graphViewStatistics;
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

        double yAxis=0, xAxis=0;
        numberOfInputData = 500;

        graphViewStatistics = (GraphView) view.findViewById(R.id.graphViewStatistics);
        inputData = new LineGraphSeries<DataPoint>();
        DataPoint[] valuesSleep = new DataPoint[numberOfInputData];


        Random random = new Random();
        for(int i=0;i < numberOfInputData; i++){
            xAxis = xAxis + 1;
            yAxis = yAxis - random.nextInt(20);
            System.out.println(yAxis);
            DataPoint vS = new DataPoint(xAxis,yAxis);
            valuesSleep[i] = vS;
            inputData.appendData(valuesSleep[i],true,numberOfInputData);
        }
        xView=xAxis;
        yView=yAxis;

        graphViewStatistics.addSeries(inputData);
        customizingGraphs();
    }

    protected void customizingGraphs(){
        inputData.setColor(Color.WHITE);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphViewStatistics);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"1", "2", "3", "4","5","6","7","8","9"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"REM", "Deep", "Middle", "Shallow"});
        graphViewStatistics.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graphViewStatistics.getViewport().setXAxisBoundsManual(true);
        graphViewStatistics.getViewport().setMinX(0);
        graphViewStatistics.getViewport().setMaxX(xView);

        graphViewStatistics.getViewport().setYAxisBoundsManual(true);
        graphViewStatistics.getViewport().setMinY(yView);
        graphViewStatistics.getViewport().setMaxY(0);

        graphViewStatistics.getGridLabelRenderer().setTextSize(35f);
        graphViewStatistics.getGridLabelRenderer().reloadStyles();
        graphViewStatistics.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#ffffff"));
        graphViewStatistics.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#ffffff"));

    }
}
