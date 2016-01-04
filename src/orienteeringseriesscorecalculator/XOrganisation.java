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
public class XOrganisation {
    
    String name = "";
    String shortName = "";
    
    String id = "";
    String code = "";

    public String getCode() {
        return code;
    }

    @XmlAttribute(name="code")
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCountry() {
        return country;
    }

    @XmlElement(name="Country")
    public void setCountry(String country) {
        this.country = country;
    }
    String country = "";
    
    public String getName(){
        return this.name;
    }
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
    
    public String getShortName(){
        if (this.shortName == null)
            return "";
        else return this.shortName;
    }
    
    @XmlElement(name="ShortName")
    public void setShortName(String shortName){
        this.shortName = shortName;
    }
    
    public String getId(){
        return this.id;
    }
    
    @XmlElement(name="Id")
    public void setId(String id){
        this.id = id;
    }
}
