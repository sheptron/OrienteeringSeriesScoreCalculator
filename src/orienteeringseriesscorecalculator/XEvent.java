/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shep
 */
@XmlRootElement(name="Event")
public class XEvent {
    
    String name = "";
    
    int raceNumber = 0;
    
    int year = 0;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
     
    public String getName() {
        return name;
    }    
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
    
    public String parseDate(){
        // This is only going to work for Twilight races which use format
        // <Name>2015-10-21RSTS_2_FaddenPines</Name>
        // for example
        if (this.name.length()>=10){
            return this.name.substring(0, 10);
        }
        else return "";
    }
    
    public int parseRaceNumber(){
        
        // This is only going to work for Twilight races which use format
        // <Name>2015-10-21RSTS_2_FaddenPines</Name>
        // for example
        
        try {
            int start =  this.name.indexOf("_");
            int stop = this.name.indexOf("_", start+1);
            if (start != -1 && stop != -1){        
                String sRaceNumber = this.name.substring(start+1, stop);        
                this.raceNumber = Integer.parseInt(sRaceNumber);       
                return this.raceNumber;
            }
            else return 1;
        }
        catch (Exception e) {
            return 1;
        }
        
    }
    
    public String parseRaceName(){
        int start =  this.name.indexOf("_");
        int stop = this.name.indexOf("_", start+1);
        String nwsName = this.name.substring(stop+1,this.name.length()); //no white space
        
        // TODO add space before capital letters (excluding the first)
        String string = insertSpaces(nwsName);
        
        // TODO remove _Final if it exists
        
        return string;
        //return this.name.substring(stop+1,this.name.length());
    }

    private String insertSpaces(String string) {
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
