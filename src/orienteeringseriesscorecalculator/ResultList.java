/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author shep
 */
@XmlRootElement(name="ResultList")
public class ResultList {
    
    String iofVersion;
    String createTime;
    String creator;
    
    Event event;
    ClassResult[] classResult; 
    
    public Event getEvent() {
        return event;
    }
    
    @XmlElement(name="Event")
    public void setEvent(Event event) {
        this.event = event;
    }

    public ClassResult[] getClassResult() {
        return classResult;
    }
    
    @XmlElement(name="ClassResult")
    public void setClassResult(ClassResult [] classResult) {
        this.classResult = classResult;
    }
    
    public String getIofVersion(){
        return this.iofVersion;
    }
    
    @XmlAttribute(name="iofVersion")
    public void setIofVersion(String iofVersion){
        this.iofVersion = iofVersion;
    }
    
    public String getCreateTime(){
        return this.createTime;
    }
    
    @XmlAttribute(name="createTime")
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    
    public String getCreator(){
        return this.creator;
    }
    
    @XmlAttribute(name="creator")
    public void setCreator(String creator){
        this.creator = creator;
    }
}