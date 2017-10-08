package com.example.maciek.sleepwell.SleepingActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.maciek.sleepwell.R;

public class ProgressActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        int i=0;
        boolean isComplete = false;

        while(!isComplete)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            i++;
            if(i==5)
            {
                isComplete=true;
            }
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
        }
    }
}
