package com.example.maciek.sleepwell.SleepingActivity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.maciek.sleepwell.DataBase.DataBase;
import com.example.maciek.sleepwell.MainActivity.MainActivity;
import com.example.maciek.sleepwell.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SleepingActivity extends AppCompatActivity {

    Button swapButton;
    TextClock textClock;
    TextView wakeUpTVSleepingActivityHour;

    private static String extraString;
    private static String wakeUpParse;
    public static String wakeUpHour;
    public static String wakeUpMinute;

    BreathMonitor breathMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        extraString = intent.getStringExtra("firstTimeOfWakeUp");
        wakeUpParse = getHourOfWakeUp(extraString);
        String[] parse = wakeUpParse.split(":");
        wakeUpHour = parse[0];
        wakeUpMinute = parse[1];


        breathMonitor = new BreathMonitor();

        swapButton = (Button) findViewById(R.id.swapButton);
        textClock = (TextClock)findViewById(R.id.textClock);
        wakeUpTVSleepingActivityHour = (TextView)findViewById(R.id.wakeUpTVSleepingActivityHour);

        setWakeUpTIme(extraString);
        textClockVisualChanges();

        swapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                breathMonitor.breathRecording.interrupt();
                synchronized (MainActivity.lock) {
                    BreathMonitor.handler.removeCallbacks(BreathMonitor.runnable);
                    MainActivity.handler.post(MainActivity.runnable);

                    MainActivity.isBackToMainActivity = true;
                    MainActivity.lock.notifyAll();
                }

                finish();
                return true;
            }
        });

        breathMonitor.startBreathMonitor();
    }


    private void textClockVisualChanges(){
        textClock.is24HourModeEnabled();
        textClock.getFormat24Hour();
    }

    private void setWakeUpTIme(String extraString){
        extraString.replace("beetwen","Alarm");
        wakeUpTVSleepingActivityHour.setText(extraString);
    }

    public String getHourOfWakeUp(String string){
        String[] parts = string.split("and");
        return parts[0].trim();
    }
}
