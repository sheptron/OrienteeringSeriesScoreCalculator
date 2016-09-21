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
    public double handicappedKmRate = 0;    // For results lists (mins/km)
    public int handicappedPlace = OrienteeringSeriesScoreCalculator.FIRST_PLACE_SCORE + 1;
    public int score = 0;
    public double handicap = 1.0;           // Assume the worst (no handicap)
    public boolean status = true;       // Innocent unless proven guilty
    
    Result(XEvent event, int _timeInSeconds, int _distanceInMetres, double _handicap){
        
        raceName = event.parseRaceName();
        raceDate = event.parseDate();
        raceNumber = event.parseRaceNumber();
        
        timeInSeconds = _timeInSeconds;
        distanceInMetres = _distanceInMetres;
        
        handicap = _handicap;
        
        this.calculateHandicappedSpeed();        
    }
    
    public void setHandicappedPlace(int place){
        handicappedPlace = place;
    }
    
    public void calculateScore(){
        // Can only score if status OK (ie no DNF/DSQ etc)
        if (this.status) {                    
            score = Math.max(OrienteeringSeriesScoreCalculator.FIRST_PLACE_SCORE + 1 - handicappedPlace, 0);     // 125 points for 1st, 124 for 2nd...
        }
        else {
            score = 0;
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        
        if (!this.status) {
            // false
            // Set speed to the maximum possible value so the athlete gets ranked last
            this.handicappedSpeed = Integer.MAX_VALUE;
        }
        else {
            this.calculateHandicappedSpeed();
        }
    }
    
    private void calculateHandicappedSpeed()
    {
        handicappedSpeed = (int) Math.round(10.0 * 1000.0 * handicap * timeInSeconds / distanceInMetres);
        
        double timeInMinutes = (timeInSeconds / 60.0);
        double distanceInKm = distanceInMetres / 1000.0;
        handicappedKmRate = Math.round(100.0 * handicap * timeInMinutes / distanceInKm) / 100.0;     
    }
}
