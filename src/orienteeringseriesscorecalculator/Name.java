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
public class Name {
    
    String family = "";
    String given = "";
    
    public String getFamily(){
        return family;
    }
    
    @XmlElement(name="Family")
    public void setFamily(String family){
        this.family = family;
    }
    
    public String getGiven(){
        return given;
    }
    
    @XmlElement(name="Given")
    public void setGiven(String given){
        this.given = given;
    }
    
}
