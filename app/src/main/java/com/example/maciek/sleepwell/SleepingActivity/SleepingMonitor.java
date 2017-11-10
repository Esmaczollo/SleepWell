package com.example.maciek.sleepwell.SleepingActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import com.example.maciek.sleepwell.MainActivity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maciek on 10.11.2017.
 */

public class SleepingMonitor extends TimerTask{

    private final Context context;
    private MediaRecorder mediaRecorder;
    private Timer timer;

    private String OUTPUT_FILE;
    private File outputFile;

    public SleepingMonitor(Context context){
        this.context = context;
        timer = new Timer();
        mediaRecorder = new MediaRecorder();

        OUTPUT_FILE = Environment.getExternalStorageDirectory() + "/audiorecorder.3gpp";
        outputFile = new File(OUTPUT_FILE);
        //every time we have brand new file
        if(outputFile.exists())
            outputFile.delete();



    }

    public void startRecording() throws IOException{
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(OUTPUT_FILE);
        //mediaRecorder.setAudioSamplingRate(10);
        mediaRecorder.prepare();
        mediaRecorder.start();

    }

    public void stopRecording(){
        if(this.mediaRecorder != null)
            mediaRecorder.stop();
    }

    public void playRecording() throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public void showAmp(){
        System.out.println(" Max amplitude: " +  mediaRecorder.getMaxAmplitude());

    }

    @Override
    public void run() {
        System.out.println(" Max amplitude: " +  mediaRecorder.getMaxAmplitude());
    }


//    @Override
//    public void run() {
//        System.out.println(" Max amplitude: " +  mediaRecorder.getMaxAmplitude());
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
}
