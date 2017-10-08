package com.example.maciek.sleepwell.Algorithms;

import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.media.MediaRecorder.AudioSource.MIC;

/**
 * Created by Maciek on 12.05.2017.
 */
/*
Jego zadaniem jest analiza oddechu użytkownika, a dokładniej amplitudy(głośności) oddechu
oraz częstotliwości oddechów na minutę. Taka analiza pozwoli na określenie
w której fazie snu znajduje się użytkownik.
 */
public class WhileSleepingAlgorithm extends MediaRecorder {

    public static int SHALLOW_NREM_AMPLITUDE, MIDDLE_NREM_AMPLITUDE, DEEP_NREM_AMPLITUDE, REM_AMPLITUDE;
    public static int SHALLOW_NREM_FREGUENCY, MIDDLE_NREM_FREGUENCY, DEEP_NREM_FREGUENCY, REM_FREGUENCY;

    public MediaRecorder mediaRecorder;

    boolean isSleeping = true;
    public int maxAmplitude,frequencyOfBreath;



    public WhileSleepingAlgorithm(){

        mediaRecorder = new MediaRecorder();
        /*
        Ustawiamy
         */
        mediaRecorder.setAudioSource(MIC);

        while (isSleeping) {
            /*
             mediaRecorder pobiera maksymalną amplitudę aktualnie przetworzonej części dźwięku
             */
            maxAmplitude = mediaRecorder.getMaxAmplitude();

            if(maxAmplitude >= SHALLOW_NREM_AMPLITUDE && frequencyOfBreath >= SHALLOW_NREM_FREGUENCY){
                shallowNREMPhase();
            }
            else if(maxAmplitude < SHALLOW_NREM_AMPLITUDE && maxAmplitude >= MIDDLE_NREM_AMPLITUDE &&
                    frequencyOfBreath < SHALLOW_NREM_FREGUENCY && frequencyOfBreath >= MIDDLE_NREM_FREGUENCY){
                middleNREMPhase();
            }
            else if(maxAmplitude < MIDDLE_NREM_AMPLITUDE && maxAmplitude >= DEEP_NREM_AMPLITUDE &&
                    frequencyOfBreath < MIDDLE_NREM_FREGUENCY && frequencyOfBreath >= DEEP_NREM_FREGUENCY){
                deepNREMPhase();
            }
            else if(maxAmplitude < MIDDLE_NREM_AMPLITUDE && maxAmplitude >= DEEP_NREM_AMPLITUDE &&
                    frequencyOfBreath < MIDDLE_NREM_FREGUENCY && frequencyOfBreath >= DEEP_NREM_FREGUENCY){
                deepNREMPhase();
            }
            else if(maxAmplitude < DEEP_NREM_AMPLITUDE && maxAmplitude >= REM_AMPLITUDE &&
                    frequencyOfBreath < DEEP_NREM_FREGUENCY && frequencyOfBreath >= REM_AMPLITUDE){
                deepNREMPhase();
            }


        }

    }

    private void shallowNREMPhase(){

    }

    private void middleNREMPhase(){

    }

    private void deepNREMPhase(){

    }


}
