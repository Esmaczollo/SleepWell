package com.example.maciek.sleepwell.MainActivity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.maciek.sleepwell.DataBase.DataBase;
import com.example.maciek.sleepwell.MainActivity.Fragments.AlarmFragment;
import com.example.maciek.sleepwell.MainActivity.Fragments.SettingsFragment;
import com.example.maciek.sleepwell.MainActivity.Fragments.StatisticsFragment;
import com.example.maciek.sleepwell.R;
import com.example.maciek.sleepwell.SleepingActivity.AudioMonitor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    private DataBase dataBase;
    private LineChart mChart;
    private static AudioMonitor audioMonitor;
    public static Thread audioRecording;

    public static boolean isBackToMainActivity;
    public static final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(this);
        audioMonitor = new AudioMonitor();
        try {
            audioMonitor.prepareRecording();
            audioMonitor.startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createChart();
        isBackToMainActivity = true;
        audioRecording = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    addEntry(audioMonitor.getMaxAmplitude());
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(!MainActivity.isBackToMainActivity){
                        synchronized(lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        audioRecording.start();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        final AlarmFragment alarmFragment = new AlarmFragment();
        final StatisticsFragment statisticsFragment = new StatisticsFragment();
        final SettingsFragment settingsFragment = new SettingsFragment();

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, alarmFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_alarm:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, alarmFragment).commit();
                        break;
                    case R.id.ic_statistics:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, statisticsFragment).commit();
                        break;
                    case R.id.ic_settings:
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, settingsFragment).commit();
                        break;
                }
                return true;
            }
        });

    }

    public static AudioMonitor getAudioMonitor(){
        return audioMonitor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_main,menu);
        return true;
    }

    private void createChart(){
        mChart = (LineChart) findViewById(R.id.chart);

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
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        //leftAxis.setAxisMaximum(20000);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.setAutoScaleMinMaxEnabled(true);
    }

    int avgValue = 0;

    private void  addEntry(float newData){
//        System.out.println(mChart.getYChartMax()); ///asdddddddddddddddddddddddddddddddddddddddddddddddasdasdasd
        //mChart.getAxisLeft().resetAxisMaximum();

        LineData data = mChart.getData();
        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(0);
            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), newData), 0);
            data.notifyDataChanged();
//            ++avgValue;
//            if(avgValue==40){
//                mChart.getAxisLeft().resetAxisMaximum();
//                mChart.getAxisLeft().resetAxisMinimum();
//                avgValue=0;
//            }


            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(40);
            mChart.moveViewTo(data.getEntryCount() - 39, 0f, YAxis.AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "");

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setDrawCircles(false);


        set.setColor(Color.argb(175,255,255,255));
        set.setLineWidth(3f);
        return set;
    }

}
