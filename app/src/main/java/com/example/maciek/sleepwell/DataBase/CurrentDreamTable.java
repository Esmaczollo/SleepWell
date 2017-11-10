package com.example.maciek.sleepwell.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 10.11.2017.
 */

public class CurrentDreamTable {

    private List<String> breathAmplitude;
    private List<String> breathDate;
    private List<String> phaseType;
    private List<String> whereWeAreInPhase;
    private List<String> phaseTrend;

    public CurrentDreamTable(){
        breathAmplitude = new ArrayList<>();
        breathDate = new ArrayList<>();
        phaseType = new ArrayList<>();
        whereWeAreInPhase = new ArrayList<>();
        phaseTrend = new ArrayList<>();
    }

    //getters
    public List<String> getBreathAmplitude() {
        return breathAmplitude;
    }

    public List<String> getBreathDate() {
        return breathDate;
    }

    public List<String> getPhaseType() {
        return phaseType;
    }

    public List<String> getWhereWeAreInPhase() {
        return whereWeAreInPhase;
    }

    public List<String> getPhaseTrend() {
        return phaseTrend;
    }

    //
    public void addBreathAmplitude(String breathAmplitude) {
        this.breathAmplitude.add(breathAmplitude);
    }

    public void addBreathDate(String breathDate) {
        this.breathDate.add(breathDate);
    }

    public void addPhaseType(String phaseType) {
        this.phaseType.add(phaseType);
    }

    public void addWhereWeAreInPhase(String whereWeAreInPhase) {
        this.whereWeAreInPhase.add(whereWeAreInPhase);
    }

    public void addPhaseTrend(String phaseTrend) {
        this.phaseTrend.add(phaseTrend);
    }

}
