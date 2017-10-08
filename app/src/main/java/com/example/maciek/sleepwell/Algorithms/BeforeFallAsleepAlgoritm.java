package com.example.maciek.sleepwell.Algorithms;

import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maciek on 13.05.2017.
 */

public class BeforeFallAsleepAlgoritm {

    final static double MEASURE_OF_STRESFULL_DAY = 1.2, MEASURE_OF_PHISICAL_ACTIVITY=0.7
            ,MEASURE_OF_PHISICAL_ACTIVITY_AND_STRESFULL_DAY=0.9;

    boolean ifAlarmStarted, ifBeforeShallowNREM;
    boolean stresfullDay, physicalActivity;

    public BeforeFallAsleepAlgoritm(){


    }

    private void minutesNeedToFallAsleep(){
        /*
        Funkcja getFallingAsleepTimes() zwraca tablicę ostatnich 7 zmieżonych czasów
         */
        int[] fallingAsleepTImes = getFallingAsleepTimes(7);
        double average,averageStresfullDay,averagePhysicalActivity;

        /*
        Uzyskujemy średnią z ostatnich 7 dni
         */
        average = calculateAverage(fallingAsleepTImes);

        if(!stresfullDay && !physicalActivity){
            neededTime(average);
        }
        else if(stresfullDay && !physicalActivity){
            average = average*MEASURE_OF_STRESFULL_DAY; // przelicznik czasu potrzebny na zaśnięcie
            neededTime(average);
        }
        else if(!stresfullDay && physicalActivity){
            average = average*MEASURE_OF_PHISICAL_ACTIVITY; // przelicznik czasu potrzebny na zaśnięcie
            neededTime(average);
        }
        else if(stresfullDay && physicalActivity){
            average = average*MEASURE_OF_PHISICAL_ACTIVITY_AND_STRESFULL_DAY; // przelicznik czasu potrzebny na zaśnięcie
            neededTime(average);
        }
    }

    private void measureTheTime(){
        /*
        Mierzy czas zasnięcia
         */
        while(ifAlarmStarted && ifBeforeShallowNREM){
            startTime(); //zaczynamy mierzyć czas

            if(!ifBeforeShallowNREM){
                stopTime(); //kończymy mierzyć czas
                break; //zatrzymuje pętlę while
            }
        }
    }
    private void startTime(){

    }

    private void stopTime(){

    }

    private int[] getFallingAsleepTimes(int i){

        int[] table = {1,2,3};

        return table;
    }

    private double calculateAverage(int[] array){

        double average=0;

        for(int i=0; i<array.length;i++){
            average+=(double)array[i];
        }
        average = average/array.length;

//        for(int i : array){
//
//        }

        return average;
    }

    private void neededTime(double average){
        Calendar calendar = Calendar.getInstance();
        Date dateOFFallAsleep, dateOfWakeUP;
        long timeOFSleep,minutes, hours, seconds;
        dateOFFallAsleep=calendar.getTime();
        dateOfWakeUP=calendar.getTime();

        /*
        Otrzymujemy czas pomiędzy zaśnięciem a wybudzeniem(aktualnie ustawionym na budziku) w milisekundach
         */
        timeOFSleep = dateOfWakeUP.getTime() - dateOFFallAsleep.getTime();
        seconds = timeOFSleep/1000;
        minutes = seconds/60 + (long)average; //average jest wyrażane w minutach
        hours = minutes/60;
        minutes = minutes%60;
        if(hours%1.5 >= 0 && hours <= 30){
            showHintAboutAlarm(false);
        }
        else{
            showHintAboutAlarm(true);
        }

    }

    private boolean isInTheCompartment(long time){

        return true;
    }

    private void showHintAboutAlarm(boolean hint){


    }

}
