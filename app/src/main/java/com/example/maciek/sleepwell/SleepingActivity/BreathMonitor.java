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

    /**
     * Określa numer próbki
     */
    long probeCounter;
    /**
     * Srednia amplituda oddechu z ostatnich 60(probeDuration) sekund
     */
    private float currentAvgOneBreathAmp;
    /**
     * Zawiera średnie amplitudy oddechów z
     */
    private List<Float> avgBreathAmpList;

    /**
     * Poczatek czasu trwania czegos co moze byc uznane za oddech
     */
    private long startTimeOneBreath;

    /**
     * Czas trwania jednego oddechu
     */
    private long timeOneBreath;
    /**
     * Lista czasów trwania poszczególnych oddechów
      */
    private List<Long> timeOneBreathList;

    /**
     * Zmienna mówiąca ile już mamy oddechów
     */
    private int oneBreathCount;
    /**
     * Pozwala na określenie numeru kolejnej przerwy pomiędzy oddechami(???)
     */
    private long oneBreathBetweenCount;
    /**
     * Zlicza ilość próbek pobranych w trakcie mierzenia jednego oddechu
     */
    private long oneBreathProbeCount;
    /**
     * Flaga określająca czy zanotowano początek nowego oddechu
     */
    private boolean isStartedOneBreath;
    /**
     * Flaga określająca czy zanotowano koniec oddechu
     */
    private boolean isFinishedOneBreath;
    /**
     * Srednia amplituda jedego oddechu
     */
    private float avgOneBreath;
    /**
     * Suma amplitud jednego oddechu
     */
    private float sumOneBreath;

    /**
     * Określa czas trwania oddechu
     */
    long timeOneBreathDuration;

    /**
     * Sredni czas trwania oddechu
     */
    long currentAvgTimeOneBreathDuration;

    /**
     * Pozwala na nauczenie sie z kilkudzesiecu pierwszysch oddechów jakią mamy początkową charakterystykę oddechu
     */
    int learnSleepTrack;

    /**
     * Okresla czy znowu musi sie uczyc charakterystki snu - używa jest to wtedy gdy sie uzytkownik przekrecił
     */
    boolean isNeedToLearn;

    /**
     * Ile oddechów nalezy zebarac zyby sie nauczył
     */
    int iteratesToLearn;

    /**
     * Określa dolny zakres dla konkretnej fazy
     */
    double phaseTimeWideLess;

    /**
     * Określa górny zakres dla konkretnej fazy
     */
    double phaseTimeWideMore;

    /**
     * Okresla czy jest to pierwszy oddech
     */
    boolean firstLoop;

    /**
     * Czas pomiędzy zdarzeniami ktore moga byc uznane za oddech
     */
    private long[] timeBetweenOneBreath;
    /**
     * Lista casów pomiędzy zdarzeniami ktore moga byc uznane za oddech
     */
    private List<Long> timeBetweenOneBreathList;




    public BreathMonitor(){
        this.audioMonitor = MainActivity.getAudioMonitor();

        currentAvgOneBreathAmp = 0;
        avgBreathAmpList = new ArrayList<>();
        probeCounter = 0;
        startTimeOneBreath = 0;
        timeOneBreathList = new ArrayList<>();
        oneBreathCount = 0;
        oneBreathProbeCount = 0;
        isStartedOneBreath = false;
        isFinishedOneBreath = false;
        avgOneBreath = 0;
        oneBreathBetweenCount = 0;
        timeOneBreath = 0;
        sumOneBreath = 0;
        timeOneBreathDuration = 0;
        currentAvgTimeOneBreathDuration = 0;
        learnSleepTrack = 0;
        isNeedToLearn = true;
        iteratesToLearn = 10;
        firstLoop = true;
        /***/
        phaseTimeWideLess = 0.5;
        phaseTimeWideMore = 1.5;
        /***/
    }


    public void startBreathMonitor(){

        breathRecording = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    probeCounter++;
                    breathMonitor(audioMonitor.getMaxAmplitude());

                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    if(MainActivity.isBackToMainActivity || SleepingActivity.isTimeToWakeUp){
//                        isTimeToWakeUp = true;
//                        synchronized(MainActivity.lock) {
//                            try {
//                                System.out.println(audioProbeCounter);
//                                MainActivity.lock.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                }
            }
        });
        breathRecording.start();
    }



    /**
     * Rozpoznaje czy wystapił oddech
     * @param amplitude
     */
    private void breathRecognizer(float amplitude){
        if(amplitude > 10){
            if(amplitude >100 && amplitude<2000){
                isStartedOneBreath = true;
                isFinishedOneBreath = false;
                oneBreathProbeCount++;
                sumOneBreath = sumOneBreath + amplitude;
            }
            else{
                isStartedOneBreath = false;
                isFinishedOneBreath = true;
            }

            if(isStartedOneBreath && oneBreathProbeCount == 1){
                startTimeOneBreath = SystemClock.elapsedRealtime();
            }

            if(isFinishedOneBreath && oneBreathProbeCount > 5){
                timeOneBreathDuration = SystemClock.elapsedRealtime() - startTimeOneBreath;

                if(!isNeedToLearn){
                    if(timeOneBreathDuration > phaseTimeWideLess * currentAvgTimeOneBreathDuration &&
                            timeOneBreathDuration < phaseTimeWideMore* currentAvgTimeOneBreathDuration){
                        oneBreathCount++;

                        timeOneBreathList.add(oneBreathCount - 1, timeOneBreathDuration);
                        avgBreathAmpList.add(oneBreathCount - 1, sumOneBreath/oneBreathProbeCount);


                        currentAvgTimeOneBreathDuration = (oneBreathCount*currentAvgTimeOneBreathDuration + timeOneBreathDuration)/(oneBreathCount + 1);
                        currentAvgOneBreathAmp = (oneBreathCount*currentAvgOneBreathAmp + sumOneBreath/oneBreathProbeCount)/(oneBreathCount + 1);

                        System.out.println("Duration: " + currentAvgTimeOneBreathDuration);
                        System.out.println("Average: " + currentAvgOneBreathAmp);
                        System.out.println("================================================\n");

                        //Zerujem powtarzalne zmienne
                        oneBreathProbeCount = 0;
                        sumOneBreath = 0;
                    }
                }else{
                    learnSleepTrack++;

                    currentAvgTimeOneBreathDuration = ((learnSleepTrack - 1)*currentAvgTimeOneBreathDuration + timeOneBreathDuration)/learnSleepTrack;
                    currentAvgOneBreathAmp = ((learnSleepTrack - 1)*currentAvgOneBreathAmp + sumOneBreath/oneBreathProbeCount)/learnSleepTrack;

                    System.out.println("Duration: " + currentAvgTimeOneBreathDuration);
                    System.out.println("Average: " + currentAvgOneBreathAmp);
                    System.out.println("================================================\n");

                    oneBreathProbeCount = 0;
                    sumOneBreath = 0;

                    if(learnSleepTrack >= iteratesToLearn){
                        isNeedToLearn = false;
                        System.out.println("Koniec nauki!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("AVG time:" + currentAvgTimeOneBreathDuration);
                        System.out.println("AVG amp:" + currentAvgOneBreathAmp);
                    }
                }
            }

        }
    }

    private void breathMonitor(float amplitude){
        //System.out.println("Amplitude: " + amplitude);

//        avgBreathAmp = avgBreathAmp + amplitude;
//        if(SystemClock.elapsedRealtime() - startTimeAvgBreathAmp > probeDuration){
//
//            avgBreathAmpList.add(avgBreathAmpCount, avgBreathAmp/probeCounter);
//            avgBreathAmpCount++;
//            //System.out.println("AVG: " + avgBreathAmpList.get(avgBreathAmpCount-1));
//
//            probeCounter = 0;
//            avgBreathAmp = 0;
//            startTimeAvgBreathAmp = SystemClock.elapsedRealtime();
//        }
        breathRecognizer(amplitude);

    }


}
/*
  if(amplitude > 100 && amplitude < 1600){
            ++oneBreathProbeCount;
            avgOneBreath = avgOneBreath + amplitude;
            isStartedOneBreath = true;
        }
        else{
            if(isStartedOneBreath){
                isFinishedOneBreath = true;
            }
            isStartedOneBreath = false;
        }

        if(oneBreathProbeCount == 1 && isStartedOneBreath){//pocz&#x105;tek oddechu
            startTimeOneBreath = SystemClock.elapsedRealtime();

            timeBetweenOneBreathList.add(oneBreathBetweenCount, SystemClock.elapsedRealtime() - timeBetweenOneBreath);
            System.out.println("TIME BETWEEN: " + timeBetweenOneBreathList.get(oneBreathBetweenCount));

        }
        if(isFinishedOneBreath && oneBreathProbeCount > 5){//zalicza cos jako oddech
            timeOneBreath = SystemClock.elapsedRealtime() - startTimeOneBreath;
            avgOneBreath = avgOneBreath/oneBreathProbeCount;

            System.out.println("===========================================");
            System.out.println("===========================================");
            System.out.println("BREATH numb: " + oneBreathCount);
            System.out.println("BREATH avg: " + avgOneBreath/ oneBreathProbeCount);
            System.out.println("BREATH time: " + timeOneBreath);

            oneBreathProbeCount = 0;
            avgOneBreath = 0;
            isFinishedOneBreath = false;
            isStartedOneBreath = false;

            ++oneBreathCount;
            ++oneBreathBetweenCount;
            timeBetweenOneBreath = SystemClock.elapsedRealtime();
        }
*/