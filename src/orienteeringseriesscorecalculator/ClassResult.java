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

    Oclass oclass;
    Course course;
    PersonResult[] personResult;

    @XmlElement(name="Class")
    public Oclass getOclass() {
        return oclass;
    }

    @XmlElement(name="Course")
    public Course getCourse() {
        return course;
    }

    @XmlElement(name="PersonResult")
    public PersonResult[] getPersonResult() {
        return personResult;
    }

}
