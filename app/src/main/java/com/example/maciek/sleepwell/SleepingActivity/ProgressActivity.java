package com.example.maciek.sleepwell.SleepingActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.maciek.sleepwell.R;

import java.io.IOException;

public class ProgressActivity extends AppCompatActivity implements Runnable{

    ProgressBar progressBar;
    AudioMonitor audioMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        audioMonitor = new AudioMonitor();
        try {
            audioMonitor.startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;
        boolean isComplete = false;

        while (!isComplete) {
            run();
            i++;
            if (i == 5) {
                isComplete = true;
            }
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
