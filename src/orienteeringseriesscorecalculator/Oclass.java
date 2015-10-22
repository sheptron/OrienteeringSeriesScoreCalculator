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
public class Oclass {
    
    int id;
    String name;
    
    @XmlElement(name="Name")
    public String getName() {
        return name;
    }
   
    @XmlElement(name="Id")
    public int getId() {
        return id;
    }
}
