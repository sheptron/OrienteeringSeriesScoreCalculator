/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author shep
 */
public class XPerson {
    
    int id = 0;
    XName name = new XName();
    String birthDate = "0000-00-00";
    
    String sex = "";
    
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
    
    public XName getName(){
        return name;
    }
    
    @XmlElement(name="Name")
    public void setName(XName name){
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
        
        return Integer.parseInt(birthDate.substring(0, 4));
    }
    
}
