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
    
    //public static handicapLookupTable; // TODO
    
    public String raceNumber;
    public String raceDate;
    public String raceName;
    public int distanceInMetres;
    public int timeInSeconds;
    public int handicappedSpeed;     // Seconds/10km (equates to seconds/km rounded to 2 decimal places)
    public int handicappedPlace = 125;
    public int score = 0;
    public double handicap;
    
    Result(Event event, int _timeInSeconds, int _distanceInMetres, double _handicap){
        
        raceName = parseRaceName(event);
        raceDate = parseDate(event);
        raceNumber = parseRaceNumber(event);
        
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
    
    private static String parseDate(Event event){
        return event.name.substring(0, 10);
    }
    
    private static String parseRaceNumber(Event event){
        int start =  event.name.indexOf("_");
        int stop = event.name.indexOf("_", start+1);
        return event.name.substring(start+1, stop);
    }
    
    private static String parseRaceName(Event event){
        int start =  event.name.indexOf("_");
        int stop = event.name.indexOf("_", start+1);
        return event.name.substring(stop+1,event.name.length());
    }
}
