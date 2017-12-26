package com.example.maciek.sleepwell.SleepingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.maciek.sleepwell.MainActivity.MainActivity;
import com.example.maciek.sleepwell.R;

public class SleepingActivity extends AppCompatActivity {

    Button swapButton;
    TextClock textClock;
    TextView wakeUpTVSleepingActivityHour;

    private static String extraString;
    public static boolean isTimeToWakeUp = false;

    BreathMonitor  breathMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        extraString = intent.getStringExtra("firstTimeOfWakeUp");

        //breathMonitor = new BreathMonitor();

        swapButton = (Button) findViewById(R.id.swapButton);
        textClock = (TextClock)findViewById(R.id.textClock);
        wakeUpTVSleepingActivityHour = (TextView)findViewById(R.id.wakeUpTVSleepingActivityHour);

        setWakeUpTIme(extraString);
        textClockVisualChanges();

        swapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                synchronized (MainActivity.lock) {
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
}
