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
public class Event {
    String name;
    
    int raceNumber;
    
    public String getName() {
        return name;
    }    
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
    
    public String parseDate(){
        return this.name.substring(0, 10);
    }
    
    public String parseRaceNumber(){
        int start =  this.name.indexOf("_");
        int stop = this.name.indexOf("_", start+1);
        
        String sRaceNumber = this.name.substring(start+1, stop);
        this.raceNumber = Integer.parseInt(sRaceNumber);
        return sRaceNumber;
    }
    
    public String parseRaceName(){
        int start =  this.name.indexOf("_");
        int stop = this.name.indexOf("_", start+1);
        return this.name.substring(stop+1,this.name.length());
    }
}
