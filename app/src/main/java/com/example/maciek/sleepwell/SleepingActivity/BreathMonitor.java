package com.example.maciek.sleepwell.SleepingActivity;

import android.os.SystemClock;

import com.example.maciek.sleepwell.MainActivity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 16.12.2017.
 */

public class BreathMonitor {

    private AudioMonitor audioMonitor;
    private Thread breathRecording;

    private boolean isTimeToWakeUp;
    private int audioProbeCounter=0;

    /**
     *
     */
    private int stopperDuration;
    /**
     * Czas przez ktory pobieramy probki do jednego avgBreathAmp
     */
    private long probeDuration;
    /**
     * Srednia amplituda oddechu z ostatnich 60(probeDuration) sekund
     */
    private float avgBreathAmp;
    /**
     * Zawiera średnie amplitudy oddechów z
     */
    private List<Float> avgBreathAmpList;
    /**
     * Lioczy probki audio
     */
    private long probeCounter;

    /**
     * Określa kolejną avgBreathAmp;
     */
    private int avgBreathAmpCount;


    private long startTimeAvgBreathAmp;
    private long startTimeOneBreath;


    public BreathMonitor(){
        this.audioMonitor = MainActivity.getAudioMonitor();
        stopperDuration = 75;
        probeDuration = 1000*15;
        avgBreathAmp = 0;
        avgBreathAmpList = new ArrayList<>();
        probeCounter = 0;
        avgBreathAmpCount = 0;
        startTimeAvgBreathAmp = 0;
        startTimeOneBreath = 0;
    }


    public void startBreathMonitor(){
        isTimeToWakeUp = false;

        breathRecording = new Thread(new Runnable() {
            @Override
            public void run() {
                startTimeAvgBreathAmp = SystemClock.elapsedRealtime();
                while(true){
                    probeCounter++;
                    breathMonitor(audioMonitor.getMaxAmplitude());

                    try {
                        breathRecording.sleep(stopperDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(MainActivity.isBackToMainActivity || SleepingActivity.isTimeToWakeUp){
                        isTimeToWakeUp = true;
                        synchronized(MainActivity.lock) {
                            try {
                                System.out.println(audioProbeCounter);
                                MainActivity.lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        breathRecording.start();
    }

    int oneBreathCount = 0;
    boolean isOneBreath = false;
    boolean isStartedOneBreath = false;
    boolean isFinishedOneBreath = false;

    private void breathRecognizer(float amplitude){
        if(amplitude>100 && amplitude <500){
            oneBreathCount++;
            isStartedOneBreath = true;
        }
        else if(amplitude<=100){
            oneBreathCount = 0;
            if(isStartedOneBreath){
                isFinishedOneBreath = true;
            }
        }

        if(oneBreathCount == 1){
            startTimeOneBreath = SystemClock.elapsedRealtime();
        }
        else if(isFinishedOneBreath && oneBreathCount > 5){
            System.out.println("BREATH: " + (SystemClock.elapsedRealtime() - startTimeOneBreath));
            oneBreathCount = 0;
            isFinishedOneBreath = false;
            isStartedOneBreath = false;
        }

    }

    private void breathMonitor(float amplitude){
        //System.out.println("Amplitude: " + amplitude);

        avgBreathAmp = avgBreathAmp + amplitude;
        if(SystemClock.elapsedRealtime() - startTimeAvgBreathAmp > probeDuration){

            avgBreathAmpList.add(avgBreathAmpCount, avgBreathAmp/probeCounter);
            avgBreathAmpCount++;
            //System.out.println("AVG: " + avgBreathAmpList.get(avgBreathAmpCount-1));

            probeCounter = 0;
            avgBreathAmp = 0;
            startTimeAvgBreathAmp = SystemClock.elapsedRealtime();
        }
        breathRecognizer(amplitude);

    }


}
