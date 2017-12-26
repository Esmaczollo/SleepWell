package com.example.maciek.sleepwell.SleepingActivity;

import android.content.Context;
import android.media.AudioRecord;
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

public class AudioMonitor extends TimerTask{

    private MediaRecorder mediaRecorder;
    private Timer timer;

    private String OUTPUT_FILE;
    private File outputFile;


    public AudioMonitor(){
        timer = new Timer();
        mediaRecorder = new MediaRecorder();

        OUTPUT_FILE = Environment.getExternalStorageDirectory() + "/audiorecorder.3gpp";
        outputFile = new File(OUTPUT_FILE);
        //every time we have brand new file
        if(outputFile.exists()){
            outputFile.delete();
        }

    }

    public void prepareRecording() throws IOException{
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(OUTPUT_FILE);
        mediaRecorder.prepare();
    }

    public void startRecording() throws IOException {
        if(this.mediaRecorder != null){
            mediaRecorder.start();
        }
    }


        public void stopRecording(){
        if(this.mediaRecorder != null){
            mediaRecorder.stop();
        }
    }

    public float getMaxAmplitude(){

        return mediaRecorder.getMaxAmplitude();
    }

    public MediaRecorder getMediaRecorder(){
        return mediaRecorder;
    }


    @Override
    public void run() {

    }
}
