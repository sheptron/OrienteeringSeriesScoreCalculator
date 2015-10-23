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
@XmlRootElement(name="Event")
public class Event {
    String name;
    
    public String getName() {
        return name;
    }    
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
}
