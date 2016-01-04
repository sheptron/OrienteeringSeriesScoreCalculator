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
@XmlRootElement(name="ClassResult")
public class XClassResult {

    XClass oclass;    // don't care
    XCourse course = new XCourse();
    XPersonResult[] personResult;
    
    public XClass getOclass() {
        return oclass;
    }

    @XmlElement(name="Class")
    public void setOclass(XClass oclass) {
        this.oclass = oclass;
    }
    
    public XCourse getCourse() {
        return course;
    }    
    
    @XmlElement(name="Course")
    public void setCourse(XCourse course){
        this.course = course;
    }

    public XPersonResult[] getPersonResult() {
        return personResult;
    }    
    
    @XmlElement(name="PersonResult")
    public void setPersonResult(XPersonResult[] personResult){
        this.personResult = personResult;
    }

}
