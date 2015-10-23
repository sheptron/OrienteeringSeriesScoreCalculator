/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import static orienteeringseriesscorecalculator.OrienteeringSeriesScoreCalculator.currentYear;

/**
 *
 * @author shep
 */
public class Person {
    
    int id;
    Name name;
    String birthDate;
    
    String sex;
    
    public Athlete.Sex getAthleteSex() {
        switch (sex) {
            case "M":
                return Athlete.Sex.Male;
            case "F" :
                return Athlete.Sex.Female;            
            default :
                return Athlete.Sex.Male;
        }
    }
    
    public String getSex(){
        return sex;
    }
    
    @XmlAttribute(name="sex")
    public void setSex(String sex){
        this.sex = sex;
    }
    
    public int getId(){
        return id;
    }
    
    @XmlElement(name="Id")
    public void setId(int id){
        this.id = id;
    }
    
    public Name getName(){
        return name;
    }
    
    @XmlElement(name="Name")
    public void setName(Name name){
        this.name = name;
    }
    
    public String getBirthDate(){
        
        return birthDate;
    }
    
    @XmlElement(name="BirthDate")
    public void setBirthDate(String birthDate){
        
        this.birthDate = birthDate;
    }
    
    public int getBirthYear(){
        
        if(birthDate == null) {
            return currentYear - 27;
        } 
          
        return Integer.parseInt(birthDate.substring(0, 4));
    }
    
}
