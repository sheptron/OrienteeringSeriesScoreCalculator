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
 * 
 */
public class Course {
    
    int id;
    String name;
    int length;
    int climb;
    int numberOfControls;
    
    public String getName() {
        return name;
    }
    
    @XmlElement(name="Name")
    public void setName(String name){
        this.name = name;
    }
    
    public int getLength() {
        return length;
    }
    
    @XmlElement(name="Length")
    public void setLength(int length){
        this.length = length;
    }
}
