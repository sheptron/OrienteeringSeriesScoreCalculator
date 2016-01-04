/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author shep
 */
public class XResult {
    
    //String startTime;           // don't care
    //String finishTime;          // don't care
    int timeInSeconds = 0;
    //int timeBehindInSeconds;    // don't care
    //int position;               // don't care
    String status = "OK";              // TODO boolean
    int controlCard = 0;
    //AssignedFee assignedFee    // don't care
    
    public int getTimeInSeconds(){
        return timeInSeconds;
    }
    
    @XmlElement(name="Time")
    public void setTimeInSeconds(int timeInSeconds){
        this.timeInSeconds = timeInSeconds;
    }
    
    public int getControlCard(){
        return controlCard;
    }
    
    @XmlElement(name="ControlCard")
    public void setControlCard(int controlCard){
        this.controlCard = controlCard;
    }
    
    public String getStatus(){
        return status;
    }
    
    @XmlElement(name="Status")
    public void setStatus(String status){
        this.status = status;
    }
}
