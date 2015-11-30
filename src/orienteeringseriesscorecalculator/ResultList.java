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
    public void setIofVersion(String iofVersion) {
        this.iofVersion = iofVersion;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    @XmlAttribute(name = "createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return this.creator;
    }

    @XmlAttribute(name = "creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void cleanClasses(String[] ALLOWED_CLASSES) {
        int numberOfClassResults = this.classResult.length;
        boolean[] classAllowed = new boolean[numberOfClassResults];
        int numberOfAllowedClasses = 0;
        for (int l = 0; l < numberOfClassResults; l++) {
            classAllowed[l] = false;
            for (int k = 0; k < ALLOWED_CLASSES.length; k++) {
                String s1 = this.classResult[l].course.name.toLowerCase();
                String s2 = ALLOWED_CLASSES[k].toLowerCase();
                if (s1.contains(s2)) {
                    classAllowed[l] = true;
                    numberOfAllowedClasses += 1;
                    break;
                }
            }
        }
        
        if (numberOfAllowedClasses == numberOfClassResults){
            return;
        }
        
        ClassResult[] newClassResults = new ClassResult[numberOfAllowedClasses];
        int l = 0;
        for (int k = 0; k < numberOfClassResults; k++) {
            if (classAllowed[k]) {
                newClassResults[l] = this.classResult[k];
                l += 1;
            }
        }
        this.classResult = newClassResults;
    }
}
