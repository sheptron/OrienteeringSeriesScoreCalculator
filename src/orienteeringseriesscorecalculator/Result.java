/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

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
    public int handicappedSpeed;     // Seconds/10km (equates to seconds/km rounded to 2 decimal places)
    public int handicappedPlace = 125;
    public int score = 0;
    public double handicap;
    
    Result(Event event, int _timeInSeconds, int _distanceInMetres, double _handicap){
        
        raceName = event.parseRaceName();
        raceDate = event.parseDate();
        raceNumber = event.parseRaceNumber();
        
        timeInSeconds = _timeInSeconds;
        distanceInMetres = _distanceInMetres;
        
        handicap = _handicap;
        
        handicappedSpeed = (int) Math.round(10.0 * 1000.0 * handicap * timeInSeconds / distanceInMetres);
    }
    
    public void setHandicappedPlace(int place){
        handicappedPlace = place;
    }
    
    public void calculateScore(){
        score = 126 - handicappedPlace;     // 125 points for 1st, 124 for 2nd...
    }
}
