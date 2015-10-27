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
    
    String name;
    String shortName;
    
    String id;
    
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
