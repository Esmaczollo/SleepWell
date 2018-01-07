package com.example.maciek.sleepwell.SleepingActivity;

import android.os.Handler;
import android.os.SystemClock;

import com.example.maciek.sleepwell.DataBase.DataBase;
import com.example.maciek.sleepwell.MainActivity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maciek on 16.12.2017.
 */

public class BreathMonitor {

    private AudioMonitor audioMonitor;
    public Thread breathRecording;
    /**
     * Użyte w Thread'ach
     */
    public static Runnable runnable;
    /**
     * Użyte w Thread'ach
     */
    public static Handler handler;

    /**
     * Od tego poziomu możemy odczytać coś jako oddech
     */
    private double amplitudeThreshold;

    /**
     * Wstrzymujemy analizę oddechu na czas analizy fazy snu
     */
    private boolean isBreathRecognizerEnable;

    /**
     * Określa czy po raz pierwszy wlączyliśmy alarm
     */
    private boolean isFirstTimerUse;

    /**
     * Określa czy jest to już czas wybudzania - pomaga przy wybudzeniu
     */
    public boolean isTimeToWakeUpFlag;

    /**
     * Srednia amplituda oddechu z ostatnich 60(probeDuration) sekund
     */
    private double currentAvgOneBreathAmp;
    /**
     * Zawiera średnie amplitudy oddechów z
     */
    private List<Float> avgOneBreathAmpList;

    /**
     * Poczatek czasu trwania czegos co moze byc uznane za oddech
     */
    private long startTimeOneBreath;

    /**
     * Lista czasów trwania poszczególnych oddechów
      */
    private List<Long> timeOneBreathList;

    /**
     * Zmienna mówiąca ile już mamy oddechów
     */
    private int oneBreathCount;

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
    double currentAvgTimeOneBreathDuration;

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
    boolean isFirstLoop;

    /**
     * Czas pomiędzy zdarzeniami ktore moga byc uznane za oddech
     */
    private long timeBetweenOneBreath;
    /**
     * Lista casów pomiędzy zdarzeniami ktore moga byc uznane za oddech
     */
    private List<Long> timeBetweenOneBreathList;
    /**
     * Sredni czas pomiedzy oddechami
     */
    private double currentAvgTimeBetweenOneBreath;

    /**
     * Zmienna pomocna przy określaniu odległości pomiędzy oddechami
     */
    boolean firstBreath;

    /**
     * Zmienna pomocna przy określaniu odległośi pomiędzy oddechami
     */
    boolean waitForSecondBreath;

    /**
     * Określa moment rozpoczęcia oddechu
     */
    private List<Long> startTimeOneBreathList;

    /**
     * Służy do wywoływania metod które wywołują się co nakiś czas
     */
    public Timer timer;

    /**
     * TimerTask związany z metodą wiadomo jaką
     */
    public TimerTask phaseOfSleepRecognizerLearningTask;

    /**
     * TimerTask związany z metodą wiadomo jaką
     */
    public TimerTask phaseOfSleepRecognizerTask;


    /**
     * Zlicza ilość okresów wywołania metody uczącej - phaseOfSleepRecognizerLearning()
     */
    private int learningCounter;

    /**
     * Zlicza ilość okresów wywołania metody rozpoznawającej fazę snu - phaseOfSleepRecognizer()
     */
    private int phaseOfSleepRecognizerCounter;

    /**
     * Wariancja średniej długości pomiędzy oddechami
     */
    private double currentAvgTimeBetweenOneBreathStandardVariance;

    /**
     * Lista standardowych odchylen
     */
    private List<Double> currentAvgTimeBetweenOneBreathStandardVarianceList;

    /**
     * Srednia amplituda otoczenia
     */
    private double avgAmbientNoise;

    /**
     * Pomaga obliczyc avgAmbientNoise
     */
    private int avgAmbientNoiseCounter;

    /**
     * pomaga obliczyc ambient noise
     */
    private long timeMate;

    //phaseOfSleepRecognizerLearning
    /**
     * Pomaga obliczyć ilość oddechów które zaszły w ciągu ostatnich 2 minut
     */
    private int lastBreathAmount;

    /**
     * Aktualna wartość owej danej
     */
    private List<Integer> breathAmount;

    /**
     * Aktualna wartość owej danej
     */
    private List<Double> avgBreathBreak;

    /**
     * Aktualna wartość owej danej
     */
    private List<Double> avgBreathTime;

    /**
     * Aktualna wartość owej danej
     */
    private List<Double> avgBreathAmplitude;

    /**
     * Srednie liczone dla konkretnych faz snu
     */
    private int currentAvgBreathAmountAWAKE;
    private double currentAvgBreathBreakAWAKE;
    private double currentaAvgBreathTimeAWAKE;
    private double curentAvgBreathAmplitudeAWAKE;
    //phaseOfSleepRecognizerLearning

    /*phaseOfSleepRecognizer*/
    private List<String> sleepPhaseList;
    private List<Long> sleepPhaseTimeList;


    private static final String AWAKE = "AWAKE";
    private static final String NREM1 = "NREM1";
    private static final String NREM2 = "NREM2";
    private static final String NREM3 = "NREM3";
    private static final String REM = "REM";
    private String currentPhase = "";

    /**
     * Aktualne wartości owej danej dla fazy NREM1
     */
    private int currentAvgBreathAmountNREM1;
    private double currentAvgBreathBreakNREM1;
    private double currentAvgBreathTimeNREM1;
    private double currentAvgBreathAmplitudeNREM1;
    private double currentAvgStandardVarianceNREM1;

    private int counterNREM1;

    /**
     * Aktualne wartości owej danej dla fazy NREM2
     */
    private int currentAvgBreathAmountNREM2;
    private double currentAvgBreathBreakNREM2;
    private double currentAvgBreathTimeNREM2;
    private double currentAvgBreathAmplitudeNREM2;
    private double currentAvgStandardVarianceNREM2;

    private int counterNREM2;

    /**
     * Aktualne wartości owej danej dla fazy NREM3
     */
    private int currentAvgBreathAmountNREM3;
    private double currentAvgBreathBreakNREM3;
    private double currentAvgBreathTimeNREM3;
    private double currentAvgBreathAmplitudeNREM3;
    private double currentAvgStandardVarianceNREM3;

    private int counterNREM3;

    /**
     * Aktualne wartości owej danej dla fazy REM
     */
    private int currentAvgBreathAmountREM;
    private double currentAvgBreathBreakREM;
    private double currentAvgBreathTimeREM;
    private double currentAvgBreathAmplitudeREM;
    private double currentAvgStandardVarianceREM;

    private int counterREM;

    /**
     * Maksymalna wariancja w fazie NREM
     */
    private double maxNREMVariance;

    /**
     * Zapamiętuje jaka była ostatnia faza snu
     */
    private String lastSleepPhase;
    /*phaseOfSleepRecognizer*/

    /**Ogarnianie daty*/
    Calendar currentTime;

    Date currentDate;
    Date wakeUpDate;
    /**Ogarnianie daty*/


    public BreathMonitor(){
        this.audioMonitor = MainActivity.getAudioMonitor();
        timer = new Timer();
        phaseOfSleepRecognizerLearningTask = new TimerTask() {
            @Override
            public void run() {
                phaseOfSleepRecognizerLearning();
            }
        };

        phaseOfSleepRecognizerTask = new TimerTask() {
            @Override
            public void run() {
                phaseOfSleepRecognizer();
            }
        };

        isBreathRecognizerEnable = true;
        isFirstTimerUse = true;
        isTimeToWakeUpFlag = false;
        amplitudeThreshold = 0;

        /**breathRecognizer()*/
        currentAvgOneBreathAmp = 0;
        avgOneBreathAmpList = new ArrayList<>();
        startTimeOneBreath = 0;
        timeOneBreathList = new ArrayList<>();
        oneBreathCount = 0;
        oneBreathProbeCount = 0;
        isStartedOneBreath = false;
        isFinishedOneBreath = false;
        sumOneBreath = 0;
        timeOneBreathDuration = 0;
        currentAvgTimeOneBreathDuration = 0;
        isNeedToLearn = true;
        iteratesToLearn = 10;
        /***/
        phaseTimeWideLess = 0.5;
        phaseTimeWideMore = 2.5;
        /***/
        timeBetweenOneBreath = 0;
        timeBetweenOneBreathList = new ArrayList<>();
        firstBreath = true;
        waitForSecondBreath = true;
        currentAvgTimeBetweenOneBreath = 0;
        startTimeOneBreathList = new ArrayList<>();
        isFirstLoop = true;
        currentAvgTimeBetweenOneBreathStandardVariance = 0;
        currentAvgTimeBetweenOneBreathStandardVarianceList = new ArrayList<>();
        /**breathRecognizer()*/

        /**ambientNoise*/
        avgAmbientNoise = 0;
        avgAmbientNoiseCounter = 0;
        timeMate = 0;
        /**ambientNoise*/


        /**phaseOfSleepRecognizerLearning*/
        learningCounter = 0;
        phaseOfSleepRecognizerCounter = 0;
        lastBreathAmount = 0;
        breathAmount = new ArrayList<>();
        avgBreathBreak = new ArrayList<>();
        avgBreathTime = new ArrayList<>();
        avgBreathAmplitude = new ArrayList<>();
        currentAvgBreathAmountAWAKE = 0;
        currentAvgBreathBreakAWAKE = 0;
        currentaAvgBreathTimeAWAKE = 0;
        curentAvgBreathAmplitudeAWAKE = 0;
        /**phaseOfSleepRecognizerLearning*/

        /**phaseOfSleepRecognizer*/
        sleepPhaseList = new ArrayList<>();
        sleepPhaseTimeList = new ArrayList<>();
        currentPhase = "";

        /**
         * Aktualne wartości owej danej dla fazy NREM1
         */
        currentAvgBreathAmountNREM1 = 0;
        currentAvgBreathBreakNREM1 = 0;
        currentAvgBreathTimeNREM1 = 0;
        currentAvgBreathAmplitudeNREM1 = 0;
        currentAvgStandardVarianceNREM1 = 0;
        counterNREM1 = 0;

        /**
         * Aktualne wartości owej danej dla fazy NREM2
         */
        currentAvgBreathAmountNREM2 = 0;
        currentAvgBreathBreakNREM2 = 0;
        currentAvgBreathTimeNREM2 = 0;
        currentAvgBreathAmplitudeNREM2 = 0;
        currentAvgStandardVarianceNREM2 = 0;
        counterNREM2 = 0;

        /**
         * Aktualne wartości owej danej dla fazy NREM3
         */
        currentAvgBreathAmountNREM3 = 0;
        currentAvgBreathBreakNREM3 = 0;
        currentAvgBreathTimeNREM3 = 0;
        currentAvgBreathAmplitudeNREM3 = 0;
        currentAvgStandardVarianceNREM3 = 0;
        counterNREM3 = 0;

        /**
         * Aktualne wartości owej danej dla fazy REM
         */
        currentAvgBreathAmountREM = 0;
        currentAvgBreathBreakREM = 0;
        currentAvgBreathTimeREM = 0;
        currentAvgBreathAmplitudeREM = 0;
        currentAvgStandardVarianceREM = 0;
        counterREM = 0;

        /**
         * Maksymalna wariancja w fazie NREM
         */
        maxNREMVariance = 0;
        lastSleepPhase = "";
        /**phaseOfSleepRecognizer*/

        /**Ogarnianie daty*/
        currentTime = Calendar.getInstance();
        currentDate = new Date();
        wakeUpDate = new Date();
        wakeUpDate = Calendar.getInstance().getTime();
        wakeUpDate.setHours(Integer.parseInt(SleepingActivity.wakeUpHour));
        wakeUpDate.setMinutes(Integer.parseInt(SleepingActivity.wakeUpMinute));
        /**Ogarnianie daty*/

    }




    public void startBreathMonitor(){

        /**
         * Dzięki temu przy pierwszym uruchomieniu metody startBreathMonitor()
         * algorytm odczeka 2 minuty zanim zacznie swoją docelową analizę oddechu - jest to czas
         * w którym uzytkownik "układa się do snu" - generuej to dodatkowe dźwięki, które mogłyby
         * utrudnić proces nauki(metoda phaseOfSleepRecognizerLearning())
         * 2 minuty czekania -> 2 minuty ambientNoise -> właściwe mierenie snu
         */
        handler = new Handler();
        breathRecording = new Thread(runnable = new Runnable() {
            @Override
            public void run() {
                if(isFirstTimerUse){
                    System.out.println("PRZED CZEKANIEM");
                    try {
                        Thread.sleep(1000*5); /***DO ZMIANY!!!!!!!!!!!!!!!!!!!!!!!*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PO CZEKANIU");

                    timeMate = SystemClock.elapsedRealtime();
                    while(SystemClock.elapsedRealtime() - timeMate <= 1000*10){ /***DO ZMIANY!!!!!!!!!!!!!!!!!!!!!!!*/
                        ambientNoise(audioMonitor.getMaxAmplitude());
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("CZAS pomiaru ambientNoise: " + (SystemClock.elapsedRealtime() - timeMate));
                    avgAmbientNoise = avgAmbientNoise/avgAmbientNoiseCounter;
                    amplitudeThreshold = avgAmbientNoise*1.15;
                    System.out.println("AMBIENT: " + avgAmbientNoise);

                    if(!Thread.currentThread().isInterrupted()){
                        timer.schedule(phaseOfSleepRecognizerLearningTask, 1000*10 , 1000*5); /***DO ZMIANY!!!!!!!!!!!!!!!!!!!!!!!*/
                    }
                    isFirstTimerUse = false;
                }

                if(isBreathRecognizerEnable){
                    breathMonitor(audioMonitor.getMaxAmplitude());
                }

                handler.postDelayed(this, 75);


                if(MainActivity.isBackToMainActivity || isTimeToWakeUpFlag) {
                    synchronized (MainActivity.lock) {
                        try {
                            MainActivity.lock.wait();
                            updateOneDreamTable();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        breathRecording.start();
    }

    /**
     * Metoda wywołująca metody badające i analizujące oddech użytkownika
     * @param amplitude
     */
    private void breathMonitor(float amplitude){
        breathRecognizer(amplitude);
    }

    /**
     * Analizuje średnią amplitudę otoczenia
     * @param amplitude
     */
    private void ambientNoise(float amplitude){
        if(amplitude < 100){
            avgAmbientNoise += amplitude;
            avgAmbientNoiseCounter++;
        }
    }



    /**
     * Rozpoznaje czy wystapił oddech
     * @param amplitude
     */
    int counter = 0;
    private void breathRecognizer(float amplitude){
        if(amplitude > 10){
            if(amplitude > amplitudeThreshold && amplitude<5000){
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

            if(isFinishedOneBreath && oneBreathProbeCount > 3){
                timeOneBreathDuration = SystemClock.elapsedRealtime() - startTimeOneBreath;
                System.out.println("Jestem tu: " + timeOneBreathDuration);

                    if(timeOneBreathDuration > 400){
                        if(isFirstLoop) {
                            oneBreathCount = 1; //To jest tylko jeden(pierwszy) oddech

                            startTimeOneBreathList.add(oneBreathCount - 1, startTimeOneBreath);
                            timeOneBreathList.add(oneBreathCount - 1, timeOneBreathDuration);
                            avgOneBreathAmpList.add(oneBreathCount - 1, sumOneBreath / oneBreathProbeCount);

                            currentAvgTimeOneBreathDuration = ((oneBreathCount - 1)*currentAvgTimeOneBreathDuration + timeOneBreathDuration)/(oneBreathCount);
                            currentAvgOneBreathAmp = ((oneBreathCount - 1)*currentAvgOneBreathAmp + sumOneBreath/oneBreathProbeCount)/(oneBreathCount);
                            isFirstLoop = false;
                        }
                        else{
                            oneBreathCount++;

                            startTimeOneBreathList.add(oneBreathCount - 1, startTimeOneBreath);
                            timeBetweenOneBreath = startTimeOneBreathList.get(oneBreathCount - 1) - startTimeOneBreathList.get(oneBreathCount - 2)
                                    - timeOneBreathList.get(oneBreathCount - 2);

                            if(timeBetweenOneBreath > 500){
                                timeOneBreathList.add(oneBreathCount - 1, timeOneBreathDuration);
                                avgOneBreathAmpList.add(oneBreathCount - 1, sumOneBreath / oneBreathProbeCount);
                                timeBetweenOneBreathList.add(oneBreathCount - 2, timeBetweenOneBreath);

                                currentAvgTimeBetweenOneBreath = ((oneBreathCount - 1)* currentAvgTimeBetweenOneBreath + timeBetweenOneBreath)/(oneBreathCount - 1);
                                currentAvgTimeOneBreathDuration = ((oneBreathCount - 1)*currentAvgTimeOneBreathDuration + timeOneBreathDuration)/(oneBreathCount);
                                currentAvgOneBreathAmp = ((oneBreathCount - 1)*currentAvgOneBreathAmp + sumOneBreath/oneBreathProbeCount)/(oneBreathCount);

//                                System.out.println("timeBetweenOneBreath: " + timeBetweenOneBreath);
//                                System.out.println("currentAvgTimeOneBreathDuration: " + currentAvgTimeOneBreathDuration);
//                                System.out.println("currentAvgOneBreathAmp: " + currentAvgOneBreathAmp);
//                                System.out.println("================================================\n");
                            }
                            else{
                                startTimeOneBreathList.remove(oneBreathCount - 1);
                                oneBreathCount--;
                            }
                        }
                    }
            }

            if(isFinishedOneBreath){
                oneBreathProbeCount = 0;
                sumOneBreath = 0;
                timeBetweenOneBreath = 0;
                timeOneBreathDuration = 0;
                startTimeOneBreath = 0;
            }

            counter++;

            if(counter==10){
                System.out.println("Mamy breathRecognizer");
                counter = 0;
            }


        }
    }


    /**
     * Metoda wywoływana przez pierwsze 10 minut od włączenia analizy snu
     */
    private void phaseOfSleepRecognizerLearning(){
        isBreathRecognizerEnable = false;
        System.out.println("Jestesmy w phaseOfSleepRecognizerLearning");
        learningCounter++;
//
//        breathAmount.add(learningCounter - 1, oneBreathCount - lastBreathAmount);
//        avgBreathBreak.add(learningCounter - 1, currentAvgTimeBetweenOneBreath);
//        avgBreathTime.add(learningCounter - 1, currentAvgTimeOneBreathDuration);
//        avgBreathAmplitude.add(learningCounter - 1, currentAvgOneBreathAmp);
//
//        lastBreathAmount = oneBreathCount;
//
//        sleepPhaseTimeList.add(learningCounter - 1, System.currentTimeMillis());
//        sleepPhaseList.add(learningCounter - 1, currentPhase);


        if(learningCounter == 5){
//            phaseOfSleepRecognizerCounter = learningCounter;
//            for(int i = 0;i <= learningCounter - 1; i++){
//                currentAvgBreathAmountAWAKE += breathAmount.get(i);
//                currentAvgBreathBreakAWAKE += avgBreathBreak.get(i);
//                currentaAvgBreathTimeAWAKE += avgBreathTime.get(i);
//                curentAvgBreathAmplitudeAWAKE += avgBreathAmplitude.get(i);
//            }
//            currentAvgBreathAmountAWAKE = currentAvgBreathAmountAWAKE /learningCounter;
//            currentAvgBreathBreakAWAKE = currentAvgBreathBreakAWAKE /learningCounter;
//            currentaAvgBreathTimeAWAKE = currentaAvgBreathTimeAWAKE /learningCounter;
//            curentAvgBreathAmplitudeAWAKE = curentAvgBreathAmplitudeAWAKE /learningCounter;
            /**
             * task phaseOfSleepRecognizerTask jest odpalany raz na 2 minuty
             * oraz startuje dopiero 2 minuty po oficjalnym włączeniu metody breathRecognizer()
             */
            isBreathRecognizerEnable = true;

            timer.cancel();
            timer = new Timer(); //tu może się coś popierdolić
            timer.schedule(phaseOfSleepRecognizerTask, 1000*10, 1000*5*2); /***DO ZMIANY!!!!!!!!!!!!!!!!!!!!!!!*/
        }
    }


    /**
     * Bada sen przez cały okers - jaka jest faza snu
     */
    private void phaseOfSleepRecognizer(){
        isBreathRecognizerEnable = false;
        for(int i=0;i<10;i++){
            System.out.println("Przeszło do phaseOfSleepRecognizer()");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        phaseOfSleepRecognizerCounter++;
//
//
//        breathAmount.add(phaseOfSleepRecognizerCounter - 1, oneBreathCount - lastBreathAmount);
//        avgBreathBreak.add(phaseOfSleepRecognizerCounter - 1, currentAvgTimeBetweenOneBreath);
//        avgBreathTime.add(phaseOfSleepRecognizerCounter - 1, currentAvgTimeOneBreathDuration);
//        avgBreathAmplitude.add(phaseOfSleepRecognizerCounter - 1, currentAvgOneBreathAmp);
//
//        for(int i = breathAmount.get(lastBreathAmount - 1); i <= oneBreathCount - 1; i++){
//            currentAvgTimeBetweenOneBreathStandardVariance += Math.pow((timeBetweenOneBreathList.get(i) - currentAvgTimeBetweenOneBreath),2);
//        }
//        currentAvgTimeBetweenOneBreathStandardVariance = Math.sqrt(currentAvgTimeBetweenOneBreathStandardVariance/breathAmount.get(phaseOfSleepRecognizerCounter - 1));
//
//        currentAvgTimeBetweenOneBreathStandardVarianceList.add(phaseOfSleepRecognizerCounter - 1, currentAvgTimeBetweenOneBreathStandardVariance);
//
//        lastBreathAmount = oneBreathCount;
//
//        /*Zaczynamy ocenianie gdzie jestesmy*/
//        if(avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1)/ currentAvgBreathBreakAWAKE > 1.1 && //Wchodzimy w NREM1
//                avgBreathTime.get(phaseOfSleepRecognizerCounter - 1)/ currentaAvgBreathTimeAWAKE < 0.9){
//            counterNREM1++;
//            currentPhase = NREM1;
//
//            /*Tutaj liczymy średnie dla fazy NREM1*/
//            currentAvgStandardVarianceNREM1 = ((counterNREM1-1)*currentAvgStandardVarianceNREM1 +
//                    currentAvgTimeBetweenOneBreathStandardVarianceList.get(phaseOfSleepRecognizerCounter - 1))/counterNREM1;
//            currentAvgBreathAmountNREM1 = ((counterNREM1-1)*currentAvgBreathAmountNREM1 + breathAmount.get(phaseOfSleepRecognizerCounter - 1))/counterNREM1;
//            currentAvgBreathBreakNREM1 =((counterNREM1-1)*currentAvgBreathBreakNREM1 + avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1))/counterNREM1;
//            currentAvgBreathTimeNREM1 = ((counterNREM1-1)*currentAvgBreathTimeNREM1 + avgBreathTime.get(phaseOfSleepRecognizerCounter - 1))/counterNREM1;
//            currentAvgBreathAmplitudeNREM1 = ((counterNREM1-1)*currentAvgBreathAmplitudeNREM1 + avgBreathAmplitude.get(phaseOfSleepRecognizerCounter - 1))/counterNREM1;
//        }
//        else if(avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1)/ currentAvgBreathBreakNREM1 > 1.05 && //Wchodzimy w NREM2
//                avgBreathTime.get(phaseOfSleepRecognizerCounter - 1)/ currentAvgBreathTimeNREM1 < 0.95){
//            counterNREM2++;
//            currentPhase = NREM2;
//
//            currentAvgStandardVarianceNREM2 = ((counterNREM1-1)*currentAvgStandardVarianceNREM2 +
//                    currentAvgTimeBetweenOneBreathStandardVarianceList.get(phaseOfSleepRecognizerCounter - 1))/counterNREM2;
//            currentAvgBreathAmountNREM2 = ((counterNREM1-1)*currentAvgBreathAmountNREM2 + breathAmount.get(phaseOfSleepRecognizerCounter - 1))/counterNREM2;
//            currentAvgBreathBreakNREM2 =((counterNREM1-1)*currentAvgBreathBreakNREM2 + avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1))/counterNREM2;
//            currentAvgBreathTimeNREM2 = ((counterNREM1-1)*currentAvgBreathTimeNREM2 + avgBreathTime.get(phaseOfSleepRecognizerCounter - 1))/counterNREM2;
//            currentAvgBreathAmplitudeNREM2 = ((counterNREM1-1)*currentAvgBreathAmplitudeNREM2 + avgBreathAmplitude.get(phaseOfSleepRecognizerCounter - 1))/counterNREM2;
//       }
//        else if(avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1)/ currentAvgBreathBreakNREM2 > 1.05 && //Wchodzimy w NREM3
//                avgBreathTime.get(phaseOfSleepRecognizerCounter - 1)/ currentAvgBreathTimeNREM2 < 0.95){
//            counterNREM3++;
//            currentPhase = NREM3;
//
//            currentAvgStandardVarianceNREM3 = ((counterNREM1-1)*currentAvgStandardVarianceNREM3 +
//                    currentAvgTimeBetweenOneBreathStandardVarianceList.get(phaseOfSleepRecognizerCounter - 1))/counterNREM3;
//            currentAvgBreathAmountNREM3 = ((counterNREM1-1)*currentAvgBreathAmountNREM3 + breathAmount.get(phaseOfSleepRecognizerCounter - 1))/counterNREM3;
//            currentAvgBreathBreakNREM3 =((counterNREM1-1)*currentAvgBreathBreakNREM3 + avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1))/counterNREM3;
//            currentAvgBreathTimeNREM3 = ((counterNREM1-1)*currentAvgBreathTimeNREM3 + avgBreathTime.get(phaseOfSleepRecognizerCounter - 1))/counterNREM3;
//            currentAvgBreathAmplitudeNREM3 = ((counterNREM1-1)*currentAvgBreathAmplitudeNREM3 + avgBreathAmplitude.get(phaseOfSleepRecognizerCounter - 1))/counterNREM3;
//        }
//        maxNREMVariance = Math.max(currentAvgStandardVarianceNREM1, currentAvgStandardVarianceNREM2);
//        maxNREMVariance = Math.max(maxNREMVariance, currentAvgStandardVarianceNREM3);
//
//        if(currentAvgTimeBetweenOneBreathStandardVariance > maxNREMVariance*1.15){
//            counterREM++;
//            currentPhase = REM;
//
//            currentAvgStandardVarianceREM = ((counterNREM1-1)*currentAvgStandardVarianceREM +
//                    currentAvgTimeBetweenOneBreathStandardVarianceList.get(phaseOfSleepRecognizerCounter - 1))/counterREM;
//            currentAvgBreathAmountREM = ((counterNREM1-1)*currentAvgBreathAmountREM + breathAmount.get(phaseOfSleepRecognizerCounter - 1))/counterREM;
//            currentAvgBreathBreakREM =((counterNREM1-1)*currentAvgBreathBreakREM + avgBreathBreak.get(phaseOfSleepRecognizerCounter - 1))/counterREM;
//            currentAvgBreathTimeREM = ((counterNREM1-1)*currentAvgBreathTimeREM + avgBreathTime.get(phaseOfSleepRecognizerCounter - 1))/counterREM;
//            currentAvgBreathAmplitudeREM = ((counterNREM1-1)*currentAvgBreathAmplitudeREM + avgBreathAmplitude.get(phaseOfSleepRecognizerCounter - 1))/counterREM;
//
//        }
//
//        if(!currentPhase.equalsIgnoreCase(sleepPhaseList.get(phaseOfSleepRecognizerCounter - 2))){
//            lastSleepPhase = sleepPhaseList.get(phaseOfSleepRecognizerCounter - 2);
//        }
//
//        sleepPhaseTimeList.add(phaseOfSleepRecognizerCounter - 1, System.currentTimeMillis());
//        sleepPhaseList.add(phaseOfSleepRecognizerCounter - 1, currentPhase);
        currentTime = Calendar.getInstance();

        currentDate.setHours(currentTime.get(Calendar.HOUR_OF_DAY));
        currentDate.setMinutes(currentTime.get(Calendar.MINUTE));

        System.out.println("TIME: " + currentDate.getHours() + ":" + currentDate.getMinutes());
        System.out.println("WAKEUPPPP: " + wakeUpDate.getHours() + ":" + wakeUpDate.getMinutes());


        if(wakeUpDate.compareTo(currentDate) <= 0){
            isTimeToWakeUpFlag = isTimeToWakeUp();
        }
        isBreathRecognizerEnable = true;
    }


    /**
     * Metoda pozwalająca na określenie najbardziej optymalnego momentu wybudzenia
     */
    private boolean isTimeToWakeUp(){
        System.out.println("Jesteśmy w isTimeToWakeUp");
//        if(sleepPhaseList.get(phaseOfSleepRecognizerCounter - 1).equalsIgnoreCase(NREM1)){
//            if(lastSleepPhase.equalsIgnoreCase(REM)){
//                return true;
//            }
//            else if(lastSleepPhase.equalsIgnoreCase(NREM2)){
//                return false;
//            }
//        }
//        else if(sleepPhaseList.get(phaseOfSleepRecognizerCounter - 1).equalsIgnoreCase(NREM2)){
//            if(lastSleepPhase.equalsIgnoreCase(NREM1)){
//                return true;
//            }
//            else if(lastSleepPhase.equalsIgnoreCase(NREM3)){
//                return false;
//            }
//        }
//        else if(sleepPhaseList.get(phaseOfSleepRecognizerCounter - 1).equalsIgnoreCase(NREM3)){
//            /**jak mamy deep sleep to zawsze czekamy*/
//            return false;
//        }
//        else if(sleepPhaseList.get(phaseOfSleepRecognizerCounter - 1).equalsIgnoreCase(REM)){
//            return true;
//        }
        return false;
    }

    /**
     * breathRecognizer -> phaseOfSleepRecognizer -> isTimeToWakeUp w ten sposób powninno się to
     * wszystko wywoływać po kolei
     */

    /**
     * Obie poniższe metody są pomocne podczas budowy wykresów
     * @return
     */
    public List<String> getSleepPhaseList(){
        return sleepPhaseList;
    }

    public List<Long> getsleepPhaseTimeList(){
        return sleepPhaseTimeList;
    }

    /**
     * Metoda update'u tablicy OneDreamTable
     */
    private void updateOneDreamTable(){
        DataBase dataBase = MainActivity.dataBase;
        String sleepID = String.valueOf(dataBase.getMaxSleepIDFromOneDreamTable());


        Date date = new Date();
        String indexTime;
        for(int index=0; index<sleepPhaseTimeList.size(); index++){
            date.setTime(sleepPhaseTimeList.get(index));
            indexTime = date.getHours() + ":" + date.getMinutes();

            dataBase.insertDataToOneDreamTable(sleepID,indexTime,sleepPhaseList.get(index));
        }
    }

}
