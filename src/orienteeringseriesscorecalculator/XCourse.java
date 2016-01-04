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
public class XCourse {
    
    int id = 0;
    String name = "";
    int length = 0;
    int climb = 0;
    int numberOfControls = 0;

    public int getId() {
        return id;
    }

    @XmlElement(name="Id")
    public void setId(int id) {
        this.id = id;
    }
    
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
    
    public int getClimb() {
        return climb;
    }

    @XmlElement(name="Climb")
    public void setClimb(int climb) {
        this.climb = climb;
    }

    public int getNumberOfControls() {
        return numberOfControls;
    }

    @XmlElement(name="NumberOfControls")
    public void setNumberOfControls(int numberOfControls) {
        this.numberOfControls = numberOfControls;
    }
}
