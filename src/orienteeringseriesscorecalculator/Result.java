/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.Event;

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
    public double handicappedKmRate = 0;    // For results lists (seconds/km)
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
    
    Result(Event event, int _timeInSeconds, int _distanceInMetres, double _handicap){
        
        raceName = parseRaceName(event);
        raceDate = parseDate(event);
        raceNumber = parseRaceNumber(event);
        
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

    public boolean getStatus() {
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
        
        //double timeInMinutes = (timeInSeconds / 60.0);
        double distanceInKm = (double) distanceInMetres / 1000.0;
        handicappedKmRate = Math.round(100.0 * handicap * timeInSeconds / distanceInKm) / 100.0;     
    }

    public double getHandicappedKmRate() {
        return handicappedKmRate;
    }    

    public int getHandicappedSpeed() {
        return handicappedSpeed;
    }

    public int getHandicappedPlace() {
        return handicappedPlace;
    }     
    
    private static String parseDate(Event event){
        // This is only going to work for Twilight races which use format
        // <Name>2015-10-21RSTS_2_FaddenPines</Name>
        // for example
        if (event.getName().length()>=10){
            return event.getName().substring(0, 10);
        }
        else return "";
    }
    
    private static int parseRaceNumber(Event event){
        
        // This is only going to work for Twilight races which use format
        // <Name>2015-10-21RSTS_2_FaddenPines</Name>
        // for example
        int raceNumber = 1;
        
        try {
            int start = event.getName().indexOf("_");
            int stop = event.getName().indexOf("_", start+1);
            if (start != -1 && stop != -1){        
                String sRaceNumber = event.getName().substring(start+1, stop);        
                raceNumber = Integer.parseInt(sRaceNumber);       
                return raceNumber;
            }
            else return 1;
        }
        catch (Exception e) {
            return 1;
        }
        
    }
    
    private static String parseRaceName(Event event){
        int start =  event.getName().indexOf("_");
        int stop = event.getName().indexOf("_", start+1);
        String nwsName = event.getName().substring(stop+1,event.getName().length()); //no white space
        
        // TODO add space before capital letters (excluding the first)
        String string = insertSpaces(nwsName);
        
        // TODO remove _Final if it exists
        
        return string;
        //return this.name.substring(stop+1,this.name.length());
    }

    private static String insertSpaces(String string) {
        // Don't insert a space before the FIRST letter!
        String outString = string.substring(0, 1);

        for (int i = 1; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                outString += " " + string.substring(i, i+1);
            } else {
                outString += string.substring(i, i+1);
            }
        }
        return outString;
    }
}
