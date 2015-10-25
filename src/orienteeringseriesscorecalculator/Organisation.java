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
public class Organisation {
    
    public String name;
    public String shortName;
    
    public String getName(){
        return name;
    }
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
    
    public String getShortName(){
        return shortName;
    }
    
    @XmlElement(name="ShortName")
    public void setShortName(String shortName){
        this.shortName = shortName;
    }
}
