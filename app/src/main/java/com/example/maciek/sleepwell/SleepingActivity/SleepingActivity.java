package com.example.maciek.sleepwell.SleepingActivity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maciek.sleepwell.MainActivity.Fragments.AlarmFragment;
import com.example.maciek.sleepwell.MainActivity.MainActivity;
import com.example.maciek.sleepwell.R;

import java.util.Timer;
import java.util.TimerTask;

public class SleepingActivity extends AppCompatActivity {

    Button swapButton;
    TextClock textClock;
    TextView wakeUpTVSleepingActivityHour;
    static String extraString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        extraString = intent.getStringExtra("firstTimeOfWakeUp");

        swapButton = (Button) findViewById(R.id.swapButton);
        textClock = (TextClock)findViewById(R.id.textClock);
        wakeUpTVSleepingActivityHour = (TextView)findViewById(R.id.wakeUpTVSleepingActivityHour);
//
//        swapButton.setOnTouchListener(new View.OnTouchListener() {
//            Thread timeDelayThread = new Thread();
//            Timer timer = new Timer();
//
//            long timeDelay =1500;
//            long swapTimeStart;
//            long swapTimeStop;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    swapTimeStart=0;
//                    swapTimeStart = System.currentTimeMillis();
//                }
//                else if(event.getAction() == MotionEvent.ACTION_UP){
//                    swapTimeStop = System.currentTimeMillis();
//                    if(swapTimeStop - swapTimeStart >1500)
//                    {
//                        final ExecuteAlgorithmsFragment executeAlgorithmsFragment = new ExecuteAlgorithmsFragment();
//                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, executeAlgorithmsFragment).commit();
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(SleepingActivity.this, MainActivity.class); // This is not working in class witch extends Fragment
//                        startActivity(intent);
//                    }
//                }
//
//                return true;
//            }
//        });

        swapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*Thats how we start new activity and kill the old one*/
                Intent intent = new Intent(SleepingActivity.this, ProgressActivity.class);
                startActivity(intent);
                return true;
            }
        });

        textClockVisualChanges();
        setWakeUpTIme(extraString);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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
