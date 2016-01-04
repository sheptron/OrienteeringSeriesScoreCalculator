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
class XClass {
    
    String resultListMode = "";
    String sex = "";
    int Id = 0;
    String Name = "";
    String ShortName = "";
    XFee[] fee;
    
    public String getResultListMode() {
        return resultListMode;
    }

    @XmlAttribute(name="resultListMode")
    public void setResultListMode(String resultListMode) {
        this.resultListMode = resultListMode;
    }

    public String getSex() {
        return sex;
    }

    @XmlAttribute(name="sex")
    public void setSex(String sex) {
        this.sex = sex;
    }    
    
    public int getId() {
        return Id;
    }

    @XmlElement(name="Id")
    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    @XmlElement(name="Name")
    public void setName(String Name) {
        this.Name = Name;
    }
    
    public String getShortName() {
        return ShortName;
    }

    @XmlElement(name="ShortName")
    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }
    
}
