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
public class ClassResult {

    //Oclass oclass;    // don't care
    Course course;
    PersonResult[] personResult;

    /*public Oclass getOclass() {
        return oclass;
    }
    
    @XmlElement(name="Class")
    public void setOclass(Oclass oclass){
        this.oclass = oclass;
    }*/
    
    public Course getCourse() {
        return course;
    }
    
    @XmlElement(name="Course")
    public void setCourse(Course course){
        this.course = course;
    }

    public PersonResult[] getPersonResult() {
        return personResult;
    }
    
    @XmlElement(name="PersonResult")
    public void setPersonResult(PersonResult[] personResult){
        this.personResult = personResult;
    }

}