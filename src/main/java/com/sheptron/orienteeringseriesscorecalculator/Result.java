/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheptron.orienteeringseriesscorecalculator;

/**
 *
 * @author shep
 */
public class Result {
    public int raceNumber;
    public String raceDate;
    public String raceName;
    public int distanceInMetres;
    public int timeInSeconds;
    public double minutesPerKm;
    public int handicappedPlace;
    public int score;
    
    Result(int _raceNumber, String _raceDate, String _raceName, int _timeInSeconds){
        raceNumber = _raceNumber;
        raceDate = _raceDate;
        raceName = _raceName;
        timeInSeconds = _timeInSeconds;
        
        timeInSeconds = 7200;   // 2 Hours
        handicappedPlace = 125;
        score = 0;
    }
    
    public void setHandicappedPlace(int place){
        handicappedPlace = place;
    }
    
    public void calculateScore(){
        score = 126 - handicappedPlace;     // 125 points for 1st, 124 for 2nd...
    }
}
