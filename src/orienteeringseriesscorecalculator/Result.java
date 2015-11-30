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
        
    public int raceNumber = 0;
    public String raceDate = "";
    public String raceName = "";
    public int distanceInMetres = 1;    // Don't make it zero just to ensure no divide-by-zero errors
    public int timeInSeconds = 0;   
    public int handicappedSpeed = 0;        // Seconds/10km (equates to seconds/km rounded to 2 decimal places)
    public int handicappedPlace = OrienteeringSeriesScoreCalculator.FIRST_PLACE_SCORE + 1;
    public int score = 0;
    public double handicap = 1.0;           // Assume the worst (no handicap)
    
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
        score = OrienteeringSeriesScoreCalculator.FIRST_PLACE_SCORE + 1 - handicappedPlace;     // 125 points for 1st, 124 for 2nd...
    }
}
